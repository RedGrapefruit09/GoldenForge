package com.redgrapefruit.goldenforge.init

import com.redgrapefruit.goldenforge.block.FragmentCleanerBlock
import com.redgrapefruit.goldenforge.block.MetalOreBlock
import com.redgrapefruit.goldenforge.blockentity.FragmentCleanerBlockEntity
import com.redgrapefruit.goldenforge.util.IInitializer
import com.redgrapefruit.goldenforge.util.registerBlock
import com.redgrapefruit.goldenforge.util.registerBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType

/** A registry for the mod's blocks (and their block items) */
object ModBlocks : IInitializer {
    // Ores
    val STEEL_ORE = MetalOreBlock(FabricBlockSettings.of(Material.METAL).hardness(1.5F), "steel")
    val DEEPSLATE_STEEL_ORE = MetalOreBlock(FabricBlockSettings.of(Material.METAL).hardness(1.8F), "steel")

    // Machines
    val FRAGMENT_CLEANER = FragmentCleanerBlock(FabricBlockSettings.of(Material.METAL).hardness(1.7F))

    // Block entities
    val FRAGMENT_CLEANER_BLOCK_ENTITY = makeType(::FragmentCleanerBlockEntity, FRAGMENT_CLEANER)

    override fun initialize() {
        registerBlock("steel_ore", STEEL_ORE)
        registerBlock("deepslate_steel_ore", DEEPSLATE_STEEL_ORE)
        registerBlock("fragment_cleaner", FRAGMENT_CLEANER)

        registerBlockEntity("fragment_cleaner", FRAGMENT_CLEANER_BLOCK_ENTITY)
    }

    private fun makeType(factory: FabricBlockEntityTypeBuilder.Factory<*>, block: Block): BlockEntityType<*> {
        return FabricBlockEntityTypeBuilder.create(factory, block).build()
    }
}
