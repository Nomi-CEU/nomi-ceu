import { Category, Commit, Parser, SubCategory } from "../../types/changelogTypes";
import { categories, combineKey, defaultIndentation, detailsKey, expandKey, noCategoryKey } from "./definitions";
import { parseCombine, parseDetails, parseExpand } from "./specialParser";
import { getChangelog } from "../../util/util";
import ChangelogData from "./changelogData";

export default async function parseParser(data: ChangelogData, parser: Parser): Promise<void> {
	const commits = await getChangelog(data.since, data.to, parser.dirs);

	for (const commit of commits) {
		if (data.shaList.has(commit.hash)) continue;

		if (data.commitFixes.has(commit.hash)) {
			const fixUpInfo = data.commitFixes.get(commit.hash);
			commit.message = fixUpInfo.newTitle;
			commit.body = fixUpInfo.newBody;
		}

		if (parser.skipCallback(commit, commit.message, commit.body)) {
			if (!parser.addSHACallback || parser.addSHACallback(commit, true)) data.shaList.add(commit.hash);
			continue;
		}

		const parsed = await parser.itemCallback(parser, commit, commit.message, commit.body);

		if (!parsed && parser.leftOverCallback) parser.leftOverCallback(commit, commit.message, commit.body, []);
		if (!parser.addSHACallback || parser.addSHACallback(commit, parsed)) data.shaList.add(commit.hash);

		if (parser.addCommitListCallback(commit, parsed)) data.commitList.push(commit);
	}
}

/**
 * Parses a commit body.
 * @param commitMessage The commit message to put into the changelog.
 * @param commitBody The commit body to parse with.
 * @param commitObject The commit object.
 * @param parser The parser object to use for parse expand/details.
 * @return parsed Returns true if contains parsing keys, false if not.
 */
export async function parseCommitBody(
	commitMessage: string,
	commitBody: string,
	commitObject: Commit,
	parser: Parser,
): Promise<boolean> {
	if (commitBody.includes(expandKey)) {
		await parseExpand(commitBody, commitObject, parser);
		return true;
	}
	if (commitBody.includes(detailsKey)) {
		await parseDetails(commitMessage, commitBody, commitObject, parser);
		return true;
	}
	if (commitBody.includes(noCategoryKey)) {
		return true;
	}
	if (commitBody.includes(combineKey)) {
		await parseCombine(commitBody, commitObject);
		return true;
	}
	return sortCommit(commitMessage, commitBody, commitObject);
}

/**
 * Adds the (commit) message to its correct category. Does not parse special effect tags.
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
			commitObject: commit,
			indentation: indentation,
		});
	});
	return true;
}

/**
 * Finds the categories that a commit fits in.
 * @param commitBody The commit body to sort with
 * @return categoryList The categories that the commit belongs in. Return undefined if no category specified via keys.
 */
export function findCategories(commitBody: string): Category[] | undefined {
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
export function findSubCategory(commitBody: string, category: Category): SubCategory {
	for (const subCategory of category.subCategories) {
		if (subCategory.commitKey !== undefined) {
			if (commitBody.includes(subCategory.commitKey)) {
				return subCategory;
			}
		}
	}
	return category.defaultSubCategory;
}
