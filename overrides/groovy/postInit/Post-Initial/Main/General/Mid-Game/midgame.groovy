import static com.nomiceu.nomilabs.groovy.GroovyHelpers.RecipeRecyclingHelpers.*

// Processing Array
replaceRecipeInput(metaitem('processing_array'),
	[[ore('circuitLuv'), item('advsolars:sunnarium'), ore('circuitLuv')],
	 [metaitem('robot.arm.iv'), metaitem('hull.iv'), metaitem('robot.arm.iv')],
	 [ore('circuitLuv'), metaitem('tool.datastick'), ore('circuitLuv')]])

// PBI Block into Ingots

recipes.addShapeless(<gregtech:meta_ingot:1010> * 9, [<gregtech:meta_block_compressed_63:2>])

// PBI Ingots into Block

recipes.addShapeless(<gregtech:meta_block_compressed_63:2>, [<gregtech:meta_ingot:1010> * 9>])
