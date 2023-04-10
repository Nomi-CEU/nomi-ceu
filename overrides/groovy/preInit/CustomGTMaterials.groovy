import gregtech.api.GregTechAPI.MaterialEvent
import gregtech.api.unification.material.Material
import gregtech.api.unification.Elements
import gregtech.api.unification.material.properties.ToolProperty
import gregtech.api.unification.material.properties.BlastProperty
import gregtech.api.fluids.fluidType.FluidType;

println("[Materials.groovy] Start GT Materials Registration")

Elements.add(130, 234, -1, null, "Omnium", "Nm", false)
Elements.add(149, 264, -1, null, "Draconium", "Dc", false)
Elements.add(149, 267, -1, null, "AwakenedDraconium", "Dc*", false)

// you *MUST* do it inside the event
event_manager.listen { MaterialEvent event ->

	new Material.Builder(32001, "draconium")
		.ingot().fluid().ore()
		.element("Draconium")
		.color(0xbe49ed).iconSet("metallic")
		.blastTemp(6800, BlastProperty.GasTier.HIGHER)
		.cableProperties(524288, 1, 0, true)
		.flags("generate_plate", "generate_rod", "generate_gear", "generate_dense")
		.build()

	new Material.Builder(32002, "awakened_draconium")
		.ingot().fluid()
		.element("AwakenedDraconium") // this is erroring for some reason?
		.color(0xf58742).iconSet("metallic")
		.flags("no_smelting", "generate_plate", "generate_rod", "generate_gear")
		.build()

	new Material.Builder(32003, "dark_steel")
		.ingot().fluid()
		.color(0x414751).iconSet("dull")
		.flags("generate_plate", "generate_rod", "generate_frame", "disable_decomposition")
		.components(material('iron') * 1)
		.build()

	new Material.Builder(32004, "omnium")
		.ingot().fluid()
		.element("Omnium")
		.color(0x84053e).iconSet("shiny")
		.cableProperties(2147483647, 64, 0, true)
		.build()

	new Material.Builder(32006, "ardite")
		.ingot().fluid()
		.color(0xad2f05).iconSet("dull")
		.flags("generate_plate")
		.components(material('red_steel') * 3, material('blaze') * 1)
		.build()


	new Material.Builder(32007, "mana")
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32008, "manyullyn")
		.ingot().fluid()
		.color(0x9949cc).iconSet("metallic")
		.flags("generate_plate")
		.components(material('ardite')* 4, material('cobalt')* 4, material('mana')* 1)
		.build()

	new Material.Builder(32010, "signalum")
		.ingot().fluid()
		.color(0xff7f0f).iconSet("shiny")
		.blastTemp(4000, BlastProperty.GasTier.MID, 120, 12800)
		.flags("generate_plate", "generate_dense", "generate_rod", "generate_gear")
		.components(material('annealed_copper')* 4, material('ardite')* 2, material('red_alloy')* 2)
		.cableProperties(32768, 1, 0, true)
		.build()

	new Material.Builder(32011, "conductive_iron")
		.ingot().fluid()
		.color(0xf7b29b).iconSet("metallic")
		.flags("generate_plate", "generate_gear")
		.components(material('iron')* 1, material('redstone')* 1)
		.cableProperties(32, 1, 0, true)
		.build()

	new Material.Builder(32012, "energetic_alloy")
		.ingot().fluid()
		.color(0xffb545).iconSet("shiny")
		.flags("generate_plate", "generate_gear")
		.blastTemp(1250, BlastProperty.GasTier.LOW, 120, 400)
		.components(material('gold')* 2, material('redstone')* 1, material('glowstone')* 1)
		.cableProperties(128, 1, 0, true)
		.build()

	new Material.Builder(32013, "vibrant_alloy")
		.ingot().fluid()
		.color(0xa4ff70).iconSet("shiny")
		.flags("generate_plate", "generate_gear", "generate_rod", "generate_bolt_screw")
		.blastTemp(1350, BlastProperty.GasTier.LOW, 120, 600)
		.components(material('energetic_alloy')* 1, material('ender_pearl')* 1)
		.cableProperties(512, 1, 0, true)
		.build()

	new Material.Builder(32014, "pulsating_iron")
		.ingot().fluid()
		.color(0x6ae26e).iconSet("shiny")
		.flags("generate_plate", "generate_gear")
		.components(material('iron')* 1)
		.cableProperties(8, 1, 0, true)
		.build()

	new Material.Builder(32015, "electrical_steel")
		.ingot().fluid()
		.color(0xb2c0c1).iconSet("metallic")
		.flags("generate_plate", "generate_gear")
		.components(material('steel')* 1, material('silicon')* 1)
		.build()

	new Material.Builder(32017, "lumium")
		.ingot().fluid()
		.color(0xf6ff99).iconSet("bright")
		.flags("generate_plate", "generate_gear", "generate_fine_wire")
		.blastTemp(4500, BlastProperty.GasTier.MID, 120, 14400)
		.components(material('tin_alloy')* 4, material('sterling_silver')* 2)
		.cableProperties(8192, 1, 0, true)
		.build()

	new Material.Builder(32018, "enderium")
		.ingot().fluid()
		.color(0x1f6b62).iconSet("shiny")
		.flags("generate_plate", "generate_gear", "generate_fine_wire")
		.blastTemp(6400, BlastProperty.GasTier.HIGHEST, 120, 20800)
		.components(material('lead')* 4, material('platinum')* 2, material('blue_steel')* 1, material('osmium')* 1)
		.cableProperties(131072, 1, 0, true)
		.build()

	new Material.Builder(32019, "electrum_flux")
		.ingot().fluid()
		.color(0xf7be20).iconSet("bright")
		.flags("generate_plate", "generate_gear")
		.blastTemp(1100)
		.components(material('electrum')* 6, material('lumium')* 1, material('signalum')* 1)
		.build()

	new Material.Builder(32021, "mithril")
		.ingot()
		.color(0x428fdb).iconSet("dull")
		.flags("generate_plate", "generate_gear", "no_unification")
		.components(material('titanium')* 1, material('mana')* 1)
		.build()

	new Material.Builder(32023, "crystal_matrix")
		.ingot().fluid()
		.color(0x70ecff).iconSet("shiny")
		.flags("generate_plate")
		.build()

	new Material.Builder(32024, "soularium")
		.ingot().fluid()
		.color(0x7c674d).iconSet("metallic")
		.flags("generate_plate")
		.components(material('gold')* 1)
		.build()

	new Material.Builder(32025, "end_steel")
		.ingot().fluid()
		.color(0xd6d980).iconSet("metallic")
		.flags("generate_plate", "generate_gear")
		.toolStats(new ToolProperty(4.0, 3.5, 1024, 3))
		.cableProperties(2048,1,0,true)
		.build()

	new Material.Builder(32026, "infinity")
		.ingot()
		.color(0x000000).iconSet("shiny")
		.flags("generate_plate")
		.components(material('neutronium')* 5)
		.build()

	material('infinity').setFormula("∞")

	new Material.Builder(32027, "microversium")
		.ingot()
		.color(0x9b61b8).iconSet("dull")
		.flags("generate_plate", "generate_frame")
		.build()

	new Material.Builder(32028, "draconic_superconductor")
		.ingot()
		.color(0xf5f0f4).iconSet("shiny")
		.cableProperties(2147483647, 4, 0, true)
		.build()

	new Material.Builder(32029, "osmiridium_8020")
		.dust().ore()
		.components(material('osmium')* 4, material('iridium')* 1)
		.colorAverage()
		.addOreByproducts(material('osmium'), material('iridium'), material('ruthenium'))
		.build()

	new Material.Builder(32030, "iridosmine_8020")
		.dust().ore()
		.components(material('iridium')* 4, material('osmium')* 1)
		.colorAverage()
		.addOreByproducts(material('iridium'), material('osmium'), material('rhodium'))
		.build()

	new Material.Builder(32031, "kaemanite")
		.dust().ore()
		.components(material('trinium')* 1, material('tantalum')* 1, material('oxygen')* 4)
		.color(0xe7413c).iconSet("bright")
		.addOreByproducts(material('niobium'), material('trinium_sulfide'), material('trinium'))
		.build()

	new Material.Builder(32032, "tungsten_trioxide")
		.dust()
		.color(0xC7D300).iconSet("dull")
		.flags("disable_decomposition")
		.components(material('tungsten')* 1, material('oxygen')* 3)
		.build()

	new Material.Builder(32033, "beryllium_oxide")
		.ingot()
		.color(0x54C757).iconSet("dull")
		.flags("generate_rod", "generate_ring")
		.components(material('beryllium')* 1, material('oxygen')* 1)
		.build()

	new Material.Builder(32034, "niobium_pentoxide")
		.dust()
		.color(0xBAB0C3).iconSet("rough")
		.components(material('niobium')* 2, material('oxygen')* 5)
		.build()

	new Material.Builder(32035, "tantalum_pentoxide")
		.dust()
		.color(0x72728A).iconSet("rough")
		.components(material('tantalum')* 2, material('oxygen')* 5)
		.build()

	new Material.Builder(32036, "fluorite")
		.dust().ore()
		.color(0xFFFC9E).iconSet("rough")
		.components(material('calcium')* 1, material('fluorine')* 2)
		.addOreByproducts(material('sphalerite'), material('bastnasite'), material('topaz'))
		.build()

	new Material.Builder(32037, "manganese_difluoride")
		.dust()
		.color(0xEF4B3D).iconSet("rough")
		.components(material('manganese')* 1, material('fluorine')* 2)
		.build()

	new Material.Builder(32038, "molybdenum_trioxide")
		.dust()
		.color(0xCBCFDA).iconSet("rough")
		.flags("disable_decomposition")
		.components(material('molybdenum')* 1, material('oxygen')* 3)
		.build()

	new Material.Builder(32039, "lead_chloride")
		.dust()
		.color(0xF3F3F3).iconSet("rough")
		.components(material('lead')* 1, material('chlorine')* 2)
		.build()

	new Material.Builder(32040, "wollastonite")
		.dust()
		.color(0xF7F7E7).iconSet("bright")
		.components(material('calcium')* 1, material('silicon')* 1, material('oxygen')* 3)
		.build()

	new Material.Builder(32041, "sodium_metavanadate")
		.dust()
		.flags("disable_decomposition")
		.color(0xe6bb22).iconSet("dull")
		.components(material('sodium')* 1, material('vanadium')* 1, material('oxygen')* 3)
		.build()

	new Material.Builder(32042, "vanadium_pentoxide")
		.dust()
		.color(0xffcf33).iconSet("rough")
		.components(material('vanadium')* 2, material('oxygen')* 5)
		.build()

	new Material.Builder(32043, "ammonium_metavanadate")
		.dust()
		.flags("disable_decomposition")
		.color(0xf7e37e).iconSet("dull")
		.components(material('nitrogen')* 1, material('hydrogen')* 4, material('vanadium')* 1, material('oxygen')* 3)
		.build()

	new Material.Builder(32044, "phthalic_anhydride")
		.dust()
		.flags("disable_decomposition")
		.color(0xeeaaee).iconSet("dull")
		.components(material('carbon')* 8, material('hydrogen')* 4, material('oxygen')* 3)
		.build()

	material('phthalic_anhydride').setFormula("C6H4(CO)2O", true)

	new Material.Builder(32045, "ethylanthraquinone")
		.dust()
		.color(0xf1e181)
		.flags("disable_decomposition")
		.components(material('carbon')* 16, material('hydrogen')* 12, material('oxygen')* 2)
		.build()

	material('ethylanthraquinone').setFormula("C6H4(CO)2C6H3(CH2CH3)", true)

	new Material.Builder(32046, "hydrogen_peroxide")
		.fluid()
		.color(0xd2ffff)
		.components(material('hydrogen')* 2, material('oxygen')* 2)
		.build()

	new Material.Builder(32047, "hydrazine")
		.fluid()
		.color(0xb50707)
		.components(material('nitrogen')* 2, material('hydrogen')* 4)
		.build()

	new Material.Builder(32048, "acetone_azine")
		.fluid()
		.color(0xa1e1e1)
		.components(material('carbon')* 6, material('hydrogen')* 12, material('nitrogen')* 2)
		.build()

	material('acetone_azine').setFormula("((CH3)2(CN))2", true)

	new Material.Builder(32049, "graphene_oxide")
		.dust()
		.flags("disable_decomposition")
		.color(0x777777).iconSet("rough")
		.components(material('graphene')* 1, material('oxygen')* 1)
		.build()

	new Material.Builder(32050, "kapton_k")
		.ingot().fluid()
		.color(0xffce52).iconSet("dull")
		.flags("generate_plate", "disable_decomposition")
		.components(material('carbon')* 22, material('hydrogen')* 10, material('nitrogen')* 2, material('oxygen')* 5)
		.build()

	material('kapton_k').setFormula("C6H2((CO)2N)2C6H4OC6H4", true)

	new Material.Builder(32051, "durene")
		.dust()
		.flags("disable_decomposition")
		.color(0x336040).iconSet("fine")
		.components(material('carbon')* 10, material('hydrogen')* 14)
		.build()

	material('durene').setFormula("C6H2(CH3)4", true)

	new Material.Builder(32052, "pyromellitic_dianhydride")
		.dust()
		.flags("disable_decomposition")
		.color(0xf0ead6).iconSet("rough")
		.components(material('carbon')* 10, material('hydrogen')* 2, material('oxygen')* 6)
		.build()

	material('pyromellitic_dianhydride').setFormula("C6H2(C2O3)2", true)

	new Material.Builder(32053, "dimethylformamide")
		.fluid()
		.color(0x42bdff)
		.components(material('carbon')* 3, material('hydrogen')* 7, material('nitrogen')* 1, material('oxygen')* 1)
		.build()

	new Material.Builder(32054, "aminophenol")
		.fluid()
		.flags("disable_decomposition")
		.color(0xff7f50)
		.components(material('carbon')* 6, material('hydrogen')* 7, material('nitrogen')* 1, material('oxygen')* 1)
		.build()

	new Material.Builder(32055, "oxydianiline")
		.dust()
		.flags("disable_decomposition")
		.color(0xf0e130).iconSet("dull")
		.components(material('carbon')* 12, material('hydrogen')* 12, material('nitrogen')* 2, material('oxygen')* 1)
		.build()

	material('oxydianiline').setFormula("O(C6H4NH2)2", true)

	new Material.Builder(32056, "antimony_pentafluoride")
		.fluid()
		.flags("disable_decomposition")
		.color(0xe3f1f1)
		.components(material('antimony')* 1, material('fluorine')* 5)
		.build()

	new Material.Builder(32057, "naquadah_oxide")
		.dust()
		.flags("disable_decomposition")
		.color(0x17ddd3).iconSet("rough")
		.components(material('naquadah')* 2, material('oxygen')* 3)
		.build()

	new Material.Builder(32058, "pyromorphite")
		.dust()
		.flags("disable_decomposition")
		.color(0xd3ed28).iconSet("rough")
		.components(material('lead')* 5, material('phosphate')* 3, material('chlorine')* 1)
		.build()

	new Material.Builder(32059, "naquadah_hydroxide")
		.dust()
		.color(0x1941a6).iconSet("dull")
		.components(material('naquadah')* 1, material('hydrogen')* 3, material('oxygen')* 3)
		.build()

	material('naquadah_hydroxide').setFormula("Nq(OH)3", true)

	new Material.Builder(32060, "snowchestite")
		.dust().ore()
		.flags("disable_decomposition")
		.color(0x274c9f).iconSet("shiny")
		.components(material('naquadah_oxide')* 3, material('pyromorphite')* 1)
		.addOreByproducts(material('chalcopyrite'), material('vanadium_magnetite'), material('naquadah_hydroxide'))
		.build()

	new Material.Builder(32061, "caesium_hydroxide")
		.dust()
		.flags("disable_decomposition")
		.color(0xbd8340).iconSet("dull")
		.components(material('caesium')* 1, material('oxygen')* 1, material('hydrogen')* 1)
		.build()

	new Material.Builder(32062, "neocryolite")
		.fluid()
		.flags("disable_decomposition")
		.color(0x3fd1aa)
		.components(material('caesium')* 3, material('naquadah')* 1, material('fluorine')* 6)
		.build()

	new Material.Builder(32063, "naquadah_oxide_petro_solution")
		.fluid()
		.flags("disable_decomposition")
		.color(0x595c70)
		.build()

	new Material.Builder(32064, "naquadah_oxide_aero_solution")
		.fluid()
		.flags("disable_decomposition")
		.color(0x6f7059)
		.build()

	new Material.Builder(32065, "hot_naquadah_oxide_neocryolite_solution")
		.fluid()
		.flags("disable_decomposition")
		.color(0x658280)
		.build()

	material('hot_naquadah_oxide_neocryolite_solution').setFluidTemperature(4700)

	new Material.Builder(32066, "lead_metasilicate")
		.dust()
		.color(0xF7F7E7).iconSet("dull")
		.components(material('lead')* 1, material('silicon')* 1, material('oxygen')* 3)
		.build()

	new Material.Builder(32067, "platinum_metallic")
		.dust()
		.color(0xfffbc5).iconSet("metallic")
		.flags("disable_decomposition")
		.components(material('platinum')* 1, material('rare_earth')* 1)
		.build()

	new Material.Builder(32068, "palladium_metallic")
		.dust()
		.color(0x808080).iconSet("metallic")
		.flags("disable_decomposition")
		.components(material('palladium')* 1, material('rare_earth')* 1)
		.build()

	new Material.Builder(32069, "ammonium_hexachloroplatinate")
		.dust()
		.color(0xfef0c2).iconSet("metallic")
		.flags("disable_decomposition")
		.components(material('nitrogen')* 2, material('hydrogen')* 8, material('platinum')* 1, material('chlorine')* 6)
		.build()

	material('ammonium_hexachloroplatinate').setFormula("(NH4)2PtCl6", true)

	new Material.Builder(32070, "chloroplatinic_acid")
		.fluid()
		.color(0xfef0c2)
		.flags("disable_decomposition")
		.components(material('hydrogen')* 2, material('platinum')* 1, material('chlorine')* 6)
		.build()

	new Material.Builder(32071, "potassium_bisulfate")
		.dust()
		.color(0xfdbd68)
		.components(material('potassium')* 1, material('hydrogen')* 1, material('sulfur')* 1, material('oxygen')* 4)
		.build()

	new Material.Builder(32072, "potassium_pyrosulfate")
		.dust()
		.color(0xfbbb66)
		.components(material('potassium')* 2, material('sulfur')* 2, material('oxygen')* 7)
		.build()

	new Material.Builder(32073, "potassium_sulfate")
		.dust()
		.color(0xf0b064).iconSet("metallic")
		.components(material('potassium')* 2, material('sulfur')* 1, material('oxygen')* 4)
		.build()

	new Material.Builder(32074, "zinc_sulfate")
		.dust()
		.color(0x846649).iconSet("fine")
		.components(material('zinc')* 1, material('sulfur')* 1, material('oxygen')* 4)
		.build()

	new Material.Builder(32075, "sodium_nitrate")
		.dust()
		.color(0x846684).iconSet("rough")
		.components(material('sodium')* 1, material('nitrogen')* 1, material('oxygen')* 3)
		.build()

	new Material.Builder(32076, "rhodium_nitrate")
		.dust()
		.color(0x776649).iconSet("fine")
		.flags("disable_decomposition")
		.components(material('rhodium')* 1, material('nitrogen')* 3, material('oxygen')* 9)
		.build()

	material('rhodium_nitrate').setFormula("Rh(NO3)3", true)

	new Material.Builder(32077, "sodium_ruthenate")
		.dust()
		.color(0x3a40cb).iconSet("shiny")
		.flags("disable_decomposition")
		.components(material('sodium')* 2, material('ruthenium')* 1, material('oxygen')* 4)
		.build()

	new Material.Builder(32078, "sodium_peroxide")
		.dust()
		.color(0xecff80).iconSet("rough")
		.components(material('sodium')* 2, material('oxygen')* 2)
		.build()

	new Material.Builder(32079, "iridium_dioxide_residue")
		.dust()
		.color(0x17182e).iconSet("rough")
		.flags("disable_decomposition")
		.components(material('iridium')* 1, material('oxygen')* 2, material('rare_earth')* 1)
		.build()

	new Material.Builder(32080, "ammonium_hexachloroiridiate")
		.dust()
		.color(0x644629).iconSet("rough")
		.flags("disable_decomposition")
		.components(material('nitrogen')* 2, material('hydrogen')* 8, material('iridium')* 1, material('chlorine')* 6)
		.build()

	material('ammonium_hexachloroiridiate').setFormula("(NH4)2IrCl6", true)

	new Material.Builder(32081, "platinum_group_residue")
		.dust()
		.color(0x64632e).iconSet("rough")
		.flags("disable_decomposition")
		.components(material('iridium')* 1, material('osmium')* 1, material('rhodium')* 1, material('ruthenium')* 1, material('rare_earth')* 1)
		.build()

	new Material.Builder(32082, "palladium_rich_ammonia")
		.fluid()
		.color(0x808080)
		.flags("disable_decomposition")
		.components(material('ammonia')* 2, material('palladium')* 1, material('chlorine')* 1)
		.build()

	new Material.Builder(32083, "crude_platinum_residue")
		.dust()
		.color(0xfffbc5).iconSet("dull")
		.flags("disable_decomposition")
		.components(material('platinum_raw')* 1)
		.build()

	new Material.Builder(32084, "crude_palladium_residue")
		.dust()
		.color(0x909090).iconSet("dull")
		.flags("disable_decomposition")
		.components(material('palladium_raw')* 1)
		.build()

	new Material.Builder(32085, "iridium_group_sludge")
		.dust()
		.color(0x644629).iconSet("dull")
		.flags("disable_decomposition")
		.components(material('iridium')* 1, material('osmium')* 1, material('ruthenium')* 1, material('rare_earth')* 1)
		.build()

	new Material.Builder(32086, "rhodium_sulfate_solution")
		.fluid()
		.color(0xffbb66)
		.flags("disable_decomposition")
		.components(material('rhodium_sulfate')* 1, material('water')* 1)
		.build()

	new Material.Builder(32087, "crude_rhodium_residue")
		.dust()
		.color(0x666666).iconSet("dull")
		.flags("disable_decomposition")
		.components(material('rhodium')* 2, material('water')* 1)
		.build()

	new Material.Builder(32088, "rhodium_salt")
		.dust()
		.color(0x848484).iconSet("shiny")
		.flags("disable_decomposition")
		.components(material('salt')* 2, material('rhodium')* 2, material('chlorine')* 6)
		.build()

	material('rhodium_salt').setFormula("(NaCl)2(RhCl3)2", true)

	new Material.Builder(32089, "acidic_iridium_dioxide_solution")
		.fluid()
		.color(0x27284e)
		.flags("disable_decomposition")
		.components(material('iridium_dioxide_residue')* 1, material('hydrochloric_acid')* 4)
		.build()

	new Material.Builder(32090, "platinum_palladium_leachate")
		.fluid()
		.color(0xffffc5)
		.flags("disable_decomposition")
		.components(material('platinum')* 1, material('palladium')* 1, material('aqua_regia')* 1)
		.build()

	new Material.Builder(32091, "methyl_formate")
		.fluid()
		.color(0xffaaaa)
		.flags("disable_decomposition")
		.components(material('carbon')* 2, material('hydrogen')* 4, material('oxygen')* 2)
		.build()

	material('methyl_formate').setFormula("HCOOCH3", true)

	new Material.Builder(32092, "formic_acid")
		.fluid()
		.color(0xffffc5)
		.flags("disable_decomposition")
		.components(material('carbon')* 1, material('hydrogen')* 2, material('oxygen')* 2)
		.build()

	material('formic_acid').setFormula("HCOOH", true)

	new Material.Builder(32093, "sodium_methoxide")
		.dust()
		.color(0xd0d0f0).iconSet("dull")
		.flags("disable_decomposition")
		.components(material('carbon')* 1, material('hydrogen')* 3, material('oxygen')* 1, material('sodium')* 1)
		.build()

	new Material.Builder(32094, "hexafluorosilicic_acid")
		.fluid()
		.color(0xd00010)
		.components(material('hydrogen')* 2, material('silicon')* 1, material('fluorine')* 6)
		.build()

	new Material.Builder(32095, "dirty_hexafluorosilicic_acid")
		.fluid()
		.color(0xe00030)
		.flags("disable_decomposition")
		.components(material('hydrogen')* 2, material('silicon')* 1, material('fluorine')* 6, material('rare_earth')* 1)
		.build()

	new Material.Builder(32096, "stone_residue")
		.dust()
		.color(0x4d4d4d).iconSet("rough")
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32097, "uncommon_residue")
		.dust()
		.color(0x4d4ded).iconSet("fine")
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32098, "oxidised_residue")
		.dust()
		.color(0xad4d4d).iconSet("fine")
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32099, "refined_residue")
		.dust()
		.color(0x2a8a21).iconSet("shiny")
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32100, "clean_inert_residue")
		.dust()
		.color(0x3bbd2f).iconSet("shiny")
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32101, "ultraacidic_residue")
		.fluid()
		.color(0xb0babf)
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32102, "xenic_acid")
		.fluid()
		.color(0xa567db)
		.components(material('xenon')* 1, material('water')* 1, material('oxygen')* 5, material('hydrogen_peroxide')* 1)
		.build()

	material('xenic_acid').setFormula("H2XeO4", true)

	new Material.Builder(32103, "dusty_helium")
		.fluid(FluidType.getByName("gas"))
		.color(0xa040af)
		.flags("disable_decomposition")
		.components(material('helium_3')* 1, material('rare_earth')* 1)
		.build()

	new Material.Builder(32104, "taranium_enriched_helium")
		.fluid(FluidType.getByName("gas")).plasma()
		.color(0x10c050)
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32105, "taranium_depleted_helium")
		.fluid(FluidType.getByName("gas"))
		.color(0x006010)
		.flags("disable_decomposition")
		.build()

	new Material.Builder(32106, "tritium_hydride")
		.fluid(FluidType.getByName("gas"))
		.color(0xd01010)
		.flags("disable_decomposition")
		.components(material('tritium')* 1, material('hydrogen')* 1)
		.build()

	new Material.Builder(32107, "helium_hydride")
		.fluid(FluidType.getByName("gas"))
		.color(0xe6d62e)
		.flags("disable_decomposition")
		.components(material('helium_3')* 1, material('hydrogen')* 1)
		.build()

	new Material.Builder(32108, "dioxygen_difluoride")
		.fluid()
		.colorAverage()
		.components(material('oxygen')* 2, material('fluorine')* 2)
		.build()

	material('dioxygen_difluoride').setFluidTemperature(80)

	new Material.Builder(32109, "taranium")
		.element("Taranium")
		.ingot().fluid()
		.color(0xff00ff).iconSet("bright")
		.flags("generate_plate", "generate_dense")
		.blastTemp(10800)
		.build()

	material('taranium').setFormula("Tn")

	new Material.Builder(32110, "darmstadtite")
		.dust().ore(2, 1)
		.colorAverage().iconSet("dull")
		.components(material('darmstadtium')* 2, material('sulfur')* 3)
		.addOreByproducts(material('rare_earth'), material('rhodium_sulfate'), material('darmstadtium'))
		.build()

	new Material.Builder(32111, "dulysite")
		.gem().ore(2, 1)
		.colorAverage().iconSet("ruby")
		.components(material('duranium')* 1, material('chlorine')* 3)
		.addOreByproducts(material('sphalerite'), material('duranium'), material('europium'))
		.build()

	new Material.Builder(32112, "butanol")
		.fluid()
		.color(0xc7af2e)
		.components(material('carbon')* 4, material('hydrogen')* 10, material('oxygen')* 1)
		.build()

	material('butanol').setFormula("C4H9OH", true)

	new Material.Builder(32113, "phosphorus_trichloride")
		.fluid()
		.color(0xe8c474)
		.components(material('phosphorus')* 1, material('chlorine')* 3)
		.build()

	new Material.Builder(32114, "phosphoryl_chloride")
		.fluid()
		.color(0xe8bb5b)
		.components(material('phosphorus')* 1, material('oxygen')* 1, material('chlorine')* 3)
		.build()

	new Material.Builder(32115, "tributyl_phosphate")
		.fluid()
		.color(0xe8c4a0)
		.components(material('carbon')* 12, material('hydrogen')* 27, material('oxygen')* 4, material('phosphorus')* 1)
		.build()

	material('tributyl_phosphate').setFormula("(C4H9O)3PO", true);

	material('rhodium_sulfate').addDust(0, 0);

	material('lutetium').addIngot();
	material('neptunium').addIngot();
	material('curium').addIngot();
	material('berkelium').addIngot();
	material('californium').addIngot();
	material('einsteinium').addIngot();
	material('graphite').addIngot();

	material('neptunium').addFluid();
	material('curium').addFluid();
	material('berkelium').addFluid();
	material('californium').addFluid();
	material('einsteinium').addFluid();
	material('nether_star').addFluid();

	material('topaz').addFlags("generate_lens")
	material('blue_topaz').addFlags("generate_lens")
	material('ender_pearl').addFlags("generate_lens")
	material('electrum').addFlags("generate_gear")
	material('neutronium').addFlags("generate_round")
	material('titanium').addFlags("generate_foil", "generate_fine_wire")
	material('stainless_steel').addFlags("generate_foil", "generate_fine_wire")
	material('naquadah_enriched').addFlags("generate_bolt_screw")
	material('naquadria').addFlags("generate_bolt_screw")
	material('redstone').addFlags("generate_dense")
	material('trinium').addFlags("generate_dense")
	material('iridium').addFlags("generate_dense")
	material('lapis').addFlags("generate_dense")
	material('graphene').addFlags("generate_foil")
	material('berkelium').addFlags("generate_frame")
	material('blue_steel').addFlags("generate_frame")
	material('ultimet').addFlags("generate_frame")
	// Prevent TE Gears from showing up
	material('tin').addFlags("generate_gear")
	material('copper').addFlags("generate_gear")
	material('gold').addFlags("generate_gear")
	material('lead').addFlags("generate_gear")
	material('nickel').addFlags("generate_gear")
	material('platinum').addFlags("generate_gear")
	material('silver').addFlags("generate_gear")
	material('emerald').addFlags("generate_gear")

	material('rhodium_sulfate').setIconSet("rough")

	material('black_steel').setFormula("(AuAgCu3)2Fe3?4", true)
	material('rhodium_plated_palladium').setFormula("((SnFe)4(CuAg4)2)2Pd3Rh", true)

}


println("[Materials.groovy] End GT Materials Registration")