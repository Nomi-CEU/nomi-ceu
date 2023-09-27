import fs from "fs";
import upath from "upath";
import { modpackManifest, rootDirectory, sharedDestDirectory } from "../../globals";
import {
	compareAndExpandManifestDependencies,
	getChangelog,
	getFileAtRevision,
	getLastGitTag,
	isEnvVariableSet,
} from "../../util/util";
import { ModpackManifest, ModpackManifestFile } from "../../types/modpackManifest";
import {
	Category,
	ChangelogMessage,
	Commit,
	ExpandedMessage,
	ModChangeInfo,
	SubCategory,
} from "../../types/changelogTypes";
import marked from "marked";
import mustache from "mustache";
import matter from "gray-matter";
import ListDiffer, { DiffResult } from "@egjs/list-differ";

const mdOptions = {
	pedantic: false,
	gfm: true,
	smartLists: true,
	smartypants: true,
	sanitize: true,
};

marked.setOptions(mdOptions);

// Final Builders
let builder: string[];

/* Values */
const defaultIndentation = "";
const indentationLevel = "  ";

/* Keys */

/* Special Handling Keys */
const skipKey = "[SKIP]";
const expandKey = "[EXPAND]";
const expandList = "messages";
const detailsKey = "[DETAILS]";
const detailsList = "details";
const noCategoryKey = "[NO CATEGORY]";

/* Sub Category Keys */
// Mode Category Keys
const normalMode: SubCategory = { commitKey: "[NM]", keyName: "Normal Mode" };
const hardMode: SubCategory = { commitKey: "[HM]", keyName: "Hard Mode" };

/* Misc Sub Category Keys */
const qolChanges: SubCategory = { commitKey: "[QOL]", keyName: "Quality of Life" };

/* Set Sub Categories (Sub Categories that do not let any commit in) */
const bothModes: SubCategory = { keyName: "Both Modes" };
const modUpdates: SubCategory = { keyName: "Mod Updates" };
const modAdditions: SubCategory = { keyName: "Mod Additions" };
const modRemovals: SubCategory = { keyName: "Mod Removals" };

/* Default Sub Categories (Sub Categories that allow any commit in) */
const emptySubCategory: SubCategory = { commitKey: "", keyName: "" };
const other: SubCategory = { commitKey: "", keyName: "Other" };

/* Category Keys: */
const breakingCategory: Category = {
	commitKey: "[BREAKING]",
	categoryName: "Breaking Changes",
	defaultSubCategory: emptySubCategory,
	subCategories: [emptySubCategory],
};
const balancingCategory: Category = {
	commitKey: "[BALANCING]",
	categoryName: "Balancing Changes",
	defaultSubCategory: bothModes,
	subCategories: [bothModes, normalMode, hardMode],
};
const performanceCategory: Category = {
	commitKey: "[PERFORMANCE]",
	categoryName: "Performance Improvements",
	defaultSubCategory: emptySubCategory,
	subCategories: [emptySubCategory],
};
const featureCategory: Category = {
	commitKey: "[FEATURE]",
	categoryName: "Feature Additions",
	defaultSubCategory: bothModes,
	subCategories: [qolChanges, bothModes, normalMode, hardMode],
};
const questBookCategory: Category = {
	commitKey: "[QB]",
	categoryName: "Quest Book Changes",
	defaultSubCategory: bothModes,
	subCategories: [bothModes, normalMode, hardMode],
};
const bugCategory: Category = {
	commitKey: "[BUG]",
	categoryName: "Bug Fixes",
	defaultSubCategory: bothModes,
	subCategories: [bothModes, normalMode, hardMode],
};
const generalCategory: Category = {
	commitKey: "[GENERAL]",
	categoryName: "General Changes",
	defaultSubCategory: other,
	subCategories: [modUpdates, modAdditions, modRemovals, other],
};
const internalCategory: Category = {
	commitKey: "[INTERNAL]",
	categoryName: "Internal Changes",
	defaultSubCategory: emptySubCategory,
	subCategories: [emptySubCategory],
};

/**
 * Category List
 * <p>
 * The order that the categories appear here will be the order that they appear in the changelog, and their priority.
 */
const categories: Category[] = [
	breakingCategory,
	balancingCategory,
	performanceCategory,
	featureCategory,
	questBookCategory,
	bugCategory,
	generalCategory,
	internalCategory,
];

/**
 * Generates a changelog based on environmental variables.
 */
export async function makeChangelog(): Promise<void> {
	let since = getLastGitTag(),
		to = "HEAD";

	// If this is a tagged build, fetch the tag before last.
	if (process.env.GITHUB_TAG) {
		since = getLastGitTag(process.env.GITHUB_TAG);
		to = process.env.GITHUB_TAG;
	}
	// Back-compat in case this crap is still around.
	else if (since == "latest-dev-preview") {
		since = getLastGitTag(since);
	}

	// Final Builders
	builder = [];

	// Initialize Category Lists
	categories.forEach((categoryKey) => {
		initializeCategorySection(categoryKey);
	});

	// List of all commits to put into changelog commit section
	const changelogCommitList: Commit[] = [];

	// Get commit log of commits which change overrides and/or manifest file
	const commitList: Commit[] = await getChangelog(since, to, [modpackManifest.overrides, "manifest.json"]);

	// Parse Commit List
	for (const commit of commitList) {
		if (commit.body) {
			if (!commit.body.includes(skipKey)) {
				if (!(await parseCommitBody(commit.message, commit.body, commit))) {
					generalCategory.changelogSection.get(other).push({
						commitMessage: commit.message,
						commitObjects: [commit],
					});
				}
				changelogCommitList.push(commit);
			}
		} else {
			generalCategory.changelogSection.get(other).push({
				commitMessage: commit.message,
				commitObjects: [commit],
			});
			changelogCommitList.push(commit);
		}
	}

	// Transform commit list into list of SHAs (to compare)
	const SHAList: string[] = [];
	commitList.forEach((commit) => {
		SHAList.push(commit.hash);
	});

	// Get all commits
	const secondaryCommitList: Commit[] = await getChangelog(since, to);

	// Parse commit list
	for (const commit of secondaryCommitList) {
		// If not in parsed SHA List, and has a body
		if (commit.body && !SHAList.includes(commit.hash)) {
			if (!commit.body.includes(skipKey)) {
				if (await parseCommitBody(commit.message, commit.body, commit)) {
					changelogCommitList.push(commit);
				}
			}
		}
	}

	// Push mod update blocks to General Changes.
	await pushModChangesToGenerals(since, to);

	// If the UPDATENOTES.md file is present, prepend it verbatim.
	if (fs.existsSync("../UPDATENOTES.md")) {
		builder.push((await fs.promises.readFile("../UPDATENOTES.md")).toString());
	}

	// Push the title.
	builder.push(`# Changes since ${since}`, "");

	// Push Sections of Changelog
	categories.forEach((category) => {
		const categoryLog: string[] = [];
		let hasValues = false;

		// Push All Sub Categories
		category.subCategories.forEach((subCategory) => {
			// Loop through key list instead of map to produce correct order
			const list = category.changelogSection.get(subCategory);
			if (list && list.length != 0) {
				// Push Key Name (only pushes if Key Name is not "")
				if (subCategory.keyName) {
					categoryLog.push(`### ${subCategory.keyName}:`);
				}

				// Sort Log
				sortCommitList(
					list,
					(message) => message.commitObjects,
					(a, b) => a.commitMessage.localeCompare(b.commitMessage),
				);

				// Push Log
				list.forEach((changelogMessage) => {
					categoryLog.push(formatChangelogMessage(changelogMessage));
					if (changelogMessage.subChangelogMessages) {
						changelogMessage.subChangelogMessages.forEach((subMessage) => {
							categoryLog.push(formatChangelogMessage(subMessage));
						});
					}
				});
				categoryLog.push("");
				hasValues = true;
			}
		});
		if (hasValues) {
			// Push Title
			builder.push(`## ${category.categoryName}:`);

			// Push previously made log
			builder.push(...categoryLog);
		}
	});

	// Sort the commit log
	sortCommitList(changelogCommitList, (commit) => commit);

	// Push the commit log
	if (changelogCommitList) {
		builder.push("## Commits");
		changelogCommitList.forEach((commit) => {
			builder.push(formatCommit(commit));
		});
	}

	// Check if the builder only contains the title.
	if (builder.length == 1) {
		builder.push("");
		builder.push("There haven't been any changes.");
	}

	if (isEnvVariableSet("CHANGELOG_BUILD")) {
		await fs.promises.writeFile(upath.join(sharedDestDirectory, "CHANGELOG.md"), builder.join("\n"));
		return fs.promises.writeFile(upath.join(sharedDestDirectory, "CHANGELOG_CF.md"), marked.parse(builder.join("\n")));
	}
	await fs.promises.writeFile(upath.join(rootDirectory, "CHANGELOG.md"), builder.join("\n"));
	return fs.promises.writeFile(upath.join(rootDirectory, "CHANGELOG_CF.md"), marked.parse(builder.join("\n")));
}

/**
 * Initializes the categorySection field of the categoryKey.
 * @param categoryKey The Category Key to grab the Sub Categories from
 */
function initializeCategorySection(categoryKey: Category): void {
	const categorySection = new Map<SubCategory, ChangelogMessage[]>();
	categoryKey.subCategories.forEach((subCategory) => {
		categorySection.set(subCategory, []);
	});
	categoryKey.changelogSection = categorySection;
}

function sortCommitList<T>(
	list: T[],
	transform: (obj: T) => Commit | Commit[] | undefined,
	backup?: (a: T, b: T) => number,
) {
	list.sort((a, b): number => {
		const commitsA = transform(a);
		const commitsB = transform(b);
		if (!commitsA || !commitsB) {
			// If either commit is undefined
			if (backup) return backup(a, b);
			return 0;
		}
		let commitA: Commit, commitB: Commit;
		if (!Array.isArray(commitsA) || !Array.isArray(commitsB)) {
			// If given values are Commits
			if (Array.isArray(commitsA) || Array.isArray(commitsB)) {
				throw new Error("Transform created an array + non array!");
			}
			commitA = commitsA;
			commitB = commitsB;
		} else if (commitsA.length !== 0 && commitsB.length !== 0) {
			// If given values are non-empty commit lists
			sortCommitList(commitsA, (commit) => commit);
			sortCommitList(commitsB, (commit) => commit);

			commitA = commitsA[0];
			commitB = commitsB[0];
		} else {
			// If some values are empty commit lists
			if (backup) return backup(a, b);
			return 0;
		}
		const dateA = new Date(commitA.date);
		const dateB = new Date(commitB.date);

		// This is reversed, so the newest commits go on top
		if (dateB.getTime() - dateA.getTime() !== 0) return dateB.getTime() - dateA.getTime();
		if (backup) return backup(a, b);
		return commitA.message.localeCompare(commitB.message);
	});
}

async function parseCommitBody(commitMessage: string, commitBody: string, commitObject: Commit): Promise<boolean> {
	if (commitBody.includes(expandKey)) {
		await deCompExpand(commitBody, commitObject);
		return true;
	}
	if (commitBody.includes(detailsKey)) {
		await deCompDetails(commitMessage, commitBody, commitObject);
		return true;
	}
	if (commitBody.includes(noCategoryKey)) {
		return true;
	}
	return sortCommit(commitMessage, commitBody, commitObject);
}

/**
 * Adds the (commit) message to its correct category. DO NOT CALL! Call `parseCommit`.
 * @param message The message to add
 * @param commitBody The body to use to sort
 * @param commit The commit object to grab date, author and SHA from
 * @param indentation The indentation of the message, if needed. Defaults to "".
 * @return added If the commit message was added to a category
 */
function sortCommit(message: string, commitBody: string, commit: Commit, indentation = defaultIndentation): boolean {
	const sortedCategories: Category[] = findCategories(commitBody);
	if (sortedCategories.length === 0) return false;

	sortedCategories.forEach((category) => {
		const subCategory = findSubCategory(commitBody, category);
		category.changelogSection.get(subCategory).push({
			commitMessage: message,
			commitObjects: [commit],
			indentation: indentation,
		});
	});
	return true;
}

function findCategories(commitBody: string): Category[] | undefined {
	const sortedCategories: Category[] = [];
	for (const category of categories) {
		if (category.commitKey !== undefined) {
			if (commitBody.includes(category.commitKey)) {
				sortedCategories.push(category);
			}
		}
	}
	return sortedCategories;
}

/**
 * Finds the correct Sub Category a commit should go in. Must be given the Category first!
 */
function findSubCategory(commitBody: string, category: Category): SubCategory {
	for (const subCategory of category.subCategories) {
		if (subCategory.commitKey !== undefined) {
			if (commitBody.includes(subCategory.commitKey)) {
				return subCategory;
			}
		}
	}
	return category.defaultSubCategory;
}

/**
 * Formats a Changelog Message
 * @param changelogMessage The message to format.
 * @return string Formatted Changelog Message
 */
function formatChangelogMessage(changelogMessage: ChangelogMessage): string {
	const indentation = changelogMessage.indentation == undefined ? defaultIndentation : changelogMessage.indentation;
	const message = changelogMessage.commitMessage;

	if (changelogMessage.commitObjects) {
		if (changelogMessage.commitObjects.length > 1) {
			const authors: string[] = [];
			const formattedCommits: string[] = [];
			changelogMessage.commitObjects.forEach((commit) => {
				if (!authors.includes(commit.author_name)) authors.push(commit.author_name);
				formattedCommits.push(`[\`${commit.hash.substring(0, 7)}\`](${commitLinkFormat}${commit.hash})`);
			});
			authors.sort();
			return `${indentation}* ${message} - **${authors.join("**, **")}** (${formattedCommits.join(", ")})`;
		}

		const commit = changelogMessage.commitObjects[0];
		const shortSHA = commit.hash.substring(0, 7);
		const author = commit.author_name;

		return `${indentation}* ${message} - **${author}** ([\`${shortSHA}\`](${commitLinkFormat}${commit.hash}))`;
	}

	return `${indentation}* ${message}`;
}

/**
 * The link to a commit, without the commit hash itself
 */
const commitLinkFormat = "https://github.com/Nomi-CEu/Nomi-CEu/commit/";

/**
 * Returns a formatted commit
 */
function formatCommit(commit: Commit): string {
	const date = new Date(commit.date).toLocaleDateString("en-us", { year: "numeric", month: "short", day: "numeric" });
	const formattedCommit = `${commit.message} - **${commit.author_name}** (${date})`;

	const shortSHA = commit.hash.substring(0, 7);

	return `* [\`${shortSHA}\`](${commitLinkFormat}${commit.hash}): ${formattedCommit}`;
}

/**
 * Decompiles a commit with 'expand'.
 */
async function deCompExpand(commitBody: string, commitObject: Commit): Promise<void> {
	let messages: ExpandedMessage[];
	try {
		messages = await parse(commitBody, expandKey, expandList);
	} catch (e) {
		console.error(
			`Failed parsing YAML in body:\n\`\`\`\n${commitBody}\`\`\`\nof commit object ${commitObject.hash} (${commitObject.message}).\nThis could be because of invalid syntax, or because the Expand Message List (key: '${expandList}') is not an array.\nSkipping...`,
		);
		return;
	}
	if (!messages || !Array.isArray(messages)) {
		console.error(
			`Expand Message List (key: '${expandList}') in body:\n\`\`\`\n${commitBody}\`\`\`\nof commit object ${commitObject.hash} (${commitObject.message}) is empty, not a list, or does not exist.\nSkipping...`,
		);
		return;
	}
	for (const message of messages) {
		if (message.messageBody) {
			if (!(await parseCommitBody(message.messageTitle, message.messageBody, commitObject))) {
				generalCategory.changelogSection
					.get(other)
					.push({ commitMessage: message.messageTitle, commitObjects: [commitObject] });
			}
		} else {
			generalCategory.changelogSection
				.get(other)
				.push({ commitMessage: message.messageTitle, commitObjects: [commitObject] });
		}
	}
}

/**
 * Decompiles a commit with 'details'.
 */
async function deCompDetails(commitMessage: string, commitBody: string, commitObject: Commit): Promise<void> {
	let sortedCategories = findCategories(commitBody);
	if (sortedCategories.length === 0) sortedCategories = [generalCategory];

	const subMessages = await deCompDetailsLevel(commitBody, commitObject);
	sortedCategories.forEach((category) => {
		const subCategory = findSubCategory(commitBody, category);

		category.changelogSection.get(subCategory).push({
			commitMessage: commitMessage,
			commitObjects: [commitObject],
			subChangelogMessages: subMessages,
		});
	});
}

async function deCompDetailsLevel(
	commitBody: string,
	commitObject: Commit,
	indentation = indentationLevel,
): Promise<ChangelogMessage[]> {
	const messages: string[] = await parse(commitBody, detailsKey, detailsList);

	const result: ChangelogMessage[] = [];

	for (const message of messages) {
		if (message.includes(detailsKey)) {
			result.push(...(await deCompDetailsLevel(message, commitObject, `${indentation}${indentationLevel}`)));
		} else {
			result.push({ commitMessage: message, commitObjects: [commitObject], indentation: indentation });
		}
	}

	return result;
}

async function parse(commitBody: string, delimiter: string, listKey: string): Promise<never[]> {
	// Remove everything before first delimiter in body
	const list = commitBody.split(delimiter);
	list.shift();
	const body = `${delimiter} ${list.join(delimiter)}`;

	// Parse
	const parseResult = matter(body, { delimiters: delimiter });

	return parseResult.data[listKey];
}

async function pushModChangesToGenerals(since: string, to: string) {
	const oldManifest: ModpackManifest = JSON.parse(getFileAtRevision("manifest.json", since));
	const newManifest: ModpackManifest = JSON.parse(getFileAtRevision("manifest.json", to));
	const comparisonResult = await compareAndExpandManifestDependencies(oldManifest, newManifest);

	const commitList = await getChangelog(since, to, ["manifest.json"]);
	const projectIDsToCommits: Map<number, Commit[]> = new Map();

	commitList.forEach((commit) => {
		const projectIDs = getChangedProjectIDs(commit.hash);
		projectIDs.forEach((id) => {
			if (projectIDsToCommits.has(id)) projectIDsToCommits.get(id).push(commit);
			else projectIDsToCommits.set(id, [commit]);
		});
	});

	[
		{
			subCategory: modAdditions,
			template: "{{ modName }}: *v{{ newVersion }}*",
			list: comparisonResult.added,
		},
		{
			subCategory: modUpdates,
			template: "{{ modName }}: *v{{ oldVersion }} => v{{ newVersion }}*",
			list: comparisonResult.modified,
		},
		{
			subCategory: modRemovals,
			template: "{{ modName }}: *v{{ oldVersion }}*",
			list: comparisonResult.removed,
		},
	].forEach((block) => {
		if (block.list.length == 0) {
			return;
		}
		const list = block.list
			// Yeet invalid project names.
			.filter((project) => !/project-\d*/.test(project.modName))
			.sort()
			.map((name) => name);

		list.forEach((info) => {
			let commits: Commit[] = undefined;
			if (info.projectID && projectIDsToCommits.has(info.projectID)) {
				commits = projectIDsToCommits.get(info.projectID);
			}
			generalCategory.changelogSection.get(block.subCategory).push({
				commitMessage: getModChangeMessage(info, block.template),
				commitObjects: commits,
			});
		});
	});
}

function getModChangeMessage(info: ModChangeInfo, template: string) {
	const oldVersion = cleanupVersion(info.oldVersion);
	const newVersion = cleanupVersion(info.newVersion);

	// If not provided with either version, return just mod name
	if (!oldVersion && !newVersion) return info.modName;

	// Replace in template
	return mustache.render(template, {
		modName: info.modName,
		oldVersion: oldVersion,
		newVersion: newVersion,
	});
}

function cleanupVersion(version: string): string {
	if (!version) return "";
	version = version.replace(/1\.12\.2|1\.12|\.jar/g, "");
	const list = version.match(/[\d+.?]+/g);
	return list[list.length - 1];
}

function getChangedProjectIDs(SHA: string): number[] {
	const change = getCommitChange(SHA);
	const projectIDs: number[] = [];

	if (!change || !change.diff) {
		return projectIDs;
	}

	// Add all unique IDs from both diff lists
	change.diff.added.forEach((index) => {
		const id = change.newManifest.files[index].projectID;
		if (!projectIDs.includes(id)) projectIDs.push(id);
	});

	change.diff.removed.forEach((index) => {
		const id = change.oldManifest.files[index].projectID;
		if (!projectIDs.includes(id)) projectIDs.push(id);
	});

	return projectIDs;
}

interface CommitChange {
	diff: DiffResult<ModpackManifestFile>;
	oldManifest: ModpackManifest;
	newManifest: ModpackManifest;
}

/**
 * Gets what a commit changed.
 * @param SHA The sha of the commit
 */
function getCommitChange(SHA: string): CommitChange {
	let oldManifest: ModpackManifest, newManifest: ModpackManifest;
	try {
		oldManifest = JSON.parse(getFileAtRevision("manifest.json", `${SHA}^`)) as ModpackManifest;
		newManifest = JSON.parse(getFileAtRevision("manifest.json", SHA)) as ModpackManifest;
	} catch (e) {
		console.error(`Failed to parse the manifest.json file at commit ${SHA} or the commit before! Skipping...`);
		return;
	}

	let result: DiffResult<ModpackManifestFile>;
	if (oldManifest && newManifest) {
		const differ = new ListDiffer(oldManifest.files, (e) => e.fileID);
		result = differ.update(newManifest.files);
	}

	return {
		diff: result,
		oldManifest: oldManifest,
		newManifest: newManifest,
	};
}
