package com.redgrapefruit.goldenforge.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import net.minecraft.util.Language
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

inline fun applyChance(chance: Int, action: () -> Unit) {
    if (Random.nextInt(101) <= chance) {
        action.invoke()
    }
}

/** Translates the given translation [key] into the current game language using the lang file's contents. */
fun translate(key: String): String {
    return Language.getInstance().get(key)
}
