package com.redgrapefruit.goldenforge.init

import com.redgrapefruit.goldenforge.util.IInitializer
import com.redgrapefruit.goldenforge.util.registerItem
import com.redgrapefruit.goldenforge.util.sharedItemSettings
import net.minecraft.item.Item

/** A registry for the mod's items. */
object ModItems : IInitializer {
    // Fragments
    val ANDESITE_FRAGMENT = Item(sharedItemSettings)
    val BLACKSTONE_FRAGMENT = Item(sharedItemSettings)
    val DIORITE_FRAGMENT = Item(sharedItemSettings)
    val GRAVEL_FRAGMENT = Item(sharedItemSettings)
    val STONE_FRAGMENT = Item(sharedItemSettings)

    val STEEL_FRAGMENT = Item(sharedItemSettings)

    override fun initialize() {
        registerItem("andesite_fragment", ANDESITE_FRAGMENT)
        registerItem("blackstone_fragment", BLACKSTONE_FRAGMENT)
        registerItem("diorite_fragment", DIORITE_FRAGMENT)
        registerItem("gravel_fragment", GRAVEL_FRAGMENT)
        registerItem("stone_fragment", STONE_FRAGMENT)
        registerItem("steel_fragment", STEEL_FRAGMENT)
    }
}
