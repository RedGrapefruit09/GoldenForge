package com.redgrapefruit.goldenforge.init

import com.redgrapefruit.goldenforge.block.MetalOreBlock
import com.redgrapefruit.goldenforge.util.IInitializer
import com.redgrapefruit.goldenforge.util.registerBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Material

/** A registry for the mod's blocks (and their block items) */
object ModBlocks : IInitializer {
    // Ores
    val STEEL_ORE = MetalOreBlock(FabricBlockSettings.of(Material.METAL).hardness(2.0F), "steel")

    override fun initialize() {
        registerBlock("steel_ore", STEEL_ORE)
    }
}
