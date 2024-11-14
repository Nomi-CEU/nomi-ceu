import static com.nomiceu.nomilabs.groovy.GroovyHelpers.RecyclingHelpers.*
import static gregtech.api.GTValues.*

// Processing Array
replaceRecipeInput(metaitem('processing_array'),
	[[ore('circuitLuv'), item('advsolars:sunnarium'), ore('circuitLuv')],
	 [metaitem('robot.arm.iv'), metaitem('hull.iv'), metaitem('robot.arm.iv')],
	 [ore('circuitLuv'), metaitem('tool.datastick'), ore('circuitLuv')]])

// Make Rare Earth Centrifuging Output Full Dusts
mods.gregtech.centrifuge.removeByInput([metaitem('dustRareEarth')], null)
mods.gregtech.centrifuge.recipeBuilder()
    .inputs(metaitem('dustRareEarth'))
    .chancedOutput(metaitem('dustCadmium'), 625, 100)
    .chancedOutput(metaitem('dustNeodymium'), 625, 100)
    .chancedOutput(metaitem('dustSamarium'), 625, 100)
    .chancedOutput(metaitem('dustCerium'), 625, 100)
    .chancedOutput(metaitem('dustYttrium'), 625, 100)
    .chancedOutput(metaitem('dustLanthanum'), 625, 100)
    .duration(64).EUt(VA[LV])
    .buildAndRegister()

// Nether Star Block -> Nether Star
crafting.addShapeless(item('minecraft:nether_star') * 9, [ore('blockNetherStar')])

// Fixed catalysts in PBI Chain
// Remove the recipes
mods.gregtech.chemical_reactor.removeByOutput([fluid('dichlorobenzidine')], null)
mods.gregtech.large_chemical_reactor.removeByOutput([fluid('dichlorobenzidine')], null)
mods.gregtech.large_chemical_reactor.removeByOutput([fluid('diaminobenzidine')], null)

// Chemical Reactor Recipe
mods.gregtech.chemical_reactor.recipeBuilder()
    .inputs(metaitem('dustTinyZinc'))  
    .fluidInputs([fluid('nitrochlorobenzene') * 2000, fluid('hydrogen') * 2000]) 
    .fluidOutputs([fluid('dichlorobenzidine') * 1000]) 
    .duration(200).EUt(VA[EV])
    .buildAndRegister()

// Large Chemical Reactor Recipe 
mods.gregtech.large_chemical_reactor.recipeBuilder()
    .inputs(metaitem('dustZinc')) 
    .fluidInputs([fluid('nitrochlorobenzene') * 18000, fluid('hydrogen') * 18000]) 
    .fluidOutputs([fluid('dichlorobenzidine') * 9000]) 
    .duration(1800).EUt(VA[EV])
    .buildAndRegister()

// Large Chemical Reactor Recipe
mods.gregtech.large_chemical_reactor.recipeBuilder()
    .inputs(metaitem('dustCopper'))  
    .fluidInputs([fluid('dichlorobenzidine') * 1000, fluid('ammonia') * 2000])
    .fluidOutputs([fluid('diaminobenzidine') * 1000, fluid('hydrochloric_acid') * 2000]) 
    .duration(100).EUt(VA[IV])
    .buildAndRegister()

