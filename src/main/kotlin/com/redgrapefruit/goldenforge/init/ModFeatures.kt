package com.redgrapefruit.goldenforge.init

import com.redgrapefruit.goldenforge.util.IInitializer
import com.redgrapefruit.goldenforge.util.registerConfiguredFeature
import com.redgrapefruit.goldenforge.util.registerOreBiomeModification
import com.redgrapefruit.goldenforge.util.registerPlacedFeature
import net.minecraft.util.registry.RegistryEntry
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.OreConfiguredFeatures
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.feature.PlacedFeature

/** A registry for the mod's features, configured features and placed features. */
object ModFeatures : IInitializer {
    // Configured features
    val CONFIGURED_STEEL_ORE = ConfiguredFeature(
        Feature.ORE,
        OreFeatureConfig(
            listOf(
                OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, ModBlocks.STEEL_ORE.defaultState),
                OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_STEEL_ORE.defaultState)
            ), 7))

    // Placed features
    val PLACED_STEEL_ORE = PlacedFeature(
        RegistryEntry.of(CONFIGURED_STEEL_ORE),
        listOf(
            CountPlacementModifier.of(16),
            SquarePlacementModifier.of(),
            HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(35))
        ))

    override fun initialize() {
        registerConfiguredFeature("steel_ore", CONFIGURED_STEEL_ORE)

        registerPlacedFeature("steel_ore", PLACED_STEEL_ORE)

        registerOreBiomeModification("steel_ore")
    }
}
