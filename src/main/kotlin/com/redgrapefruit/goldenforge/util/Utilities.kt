package com.redgrapefruit.goldenforge.util

import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Language
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.PlacedFeature
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.random.Random

// Miscellaneous utilities

/** Typed mod ID, use for Identifiers */
const val MOD_ID = "goldenforge"

/** Pretty full name of the mod */
const val MOD_NAME = "GoldenForge"

/** Shared [Logger] used by the mod. */
val logger: Logger = LogManager.getLogger(MOD_NAME)

/** Easily accessible [FabricLoader] instance upon demand. */
val loader: FabricLoader by lazy { FabricLoader.getInstance() }

/** Obtains the mod version of the given [mod] with the Fabric Loader API.
 *  The resulting version is equal to the one specified in the `fabric.mod.json` file */
fun FabricLoader.getModVersion(mod: String): String {
    return getModContainer(mod)
        .orElseThrow { RuntimeException("Mod $mod isn't present!") }
        .metadata
        .version
        .friendlyString
}

/** Converts this [String] into an [Identifier] with the namespace being [MOD_ID] */
inline val String.id: Identifier
    get() = Identifier(MOD_ID, this)

/** Converts this [String] into an [Identifier], given that the string has the ID form, for example, "minecraft:shears". */
inline val String.parsedId: Identifier
    get() = Identifier.tryParse(this) ?: throw RuntimeException("Invalid Identifier form: '$this'")

/** Executes the given [action] randomly with the inputted percentage [chance]. */
inline fun applyChance(chance: Int, action: () -> Unit) {
    if (Random.nextInt(101) <= chance) {
        action.invoke()
    }
}

/** Translates the given translation [key] into the current game language using the lang file's contents. */
fun translate(key: String): String {
    return Language.getInstance().get(key)
}

// Registering / Initialization

/** Item group / Creative tab for the mod's items. */
val sharedItemGroup = FabricItemGroupBuilder
    .create("main".id)
    .icon { Items.IRON_ORE.defaultStack }
    .build()

/** Base item settings applied to every item. */
val sharedItemSettings = Item.Settings().group(sharedItemGroup)

/**
 *  Defines an `object` in the `init` package that performs registering of some type of objects.
 *  One [IInitializer] should only be responsible for one type of objects.
 */
interface IInitializer {
    /** Run the registering code in the implementation of this method */
    fun initialize()
}

// Registering Helpers

fun registerBlock(name: String, block: Block) {
    Registry.register(Registry.BLOCK, name.id, block)
    val blockItem = BlockItem(block, sharedItemSettings)
    Registry.register(Registry.ITEM, name.id, blockItem)
    Item.BLOCK_ITEMS[block] = blockItem // why was this necessary?
}

fun registerItem(name: String, item: Item) {
    Registry.register(Registry.ITEM, name.id, item)
}

fun registerConfiguredFeature(name: String, feature: ConfiguredFeature<*, *>) {
    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, name.id, feature)
}

fun registerPlacedFeature(name: String, feature: PlacedFeature) {
    Registry.register(BuiltinRegistries.PLACED_FEATURE, name.id, feature)
}

fun registerOreBiomeModification(name: String) {
    BiomeModifications.addFeature(
        BiomeSelectors.foundInOverworld(),
        GenerationStep.Feature.UNDERGROUND_ORES,
        RegistryKey.of(Registry.PLACED_FEATURE_KEY, name.id))
}
