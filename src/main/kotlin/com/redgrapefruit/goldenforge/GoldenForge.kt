package com.redgrapefruit.goldenforge

import com.redgrapefruit.datapipe.PipeResourceLoader
import com.redgrapefruit.goldenforge.core.MetalConfigLoader
import com.redgrapefruit.goldenforge.init.ModBlocks
import com.redgrapefruit.goldenforge.init.ModItems
import com.redgrapefruit.goldenforge.util.MOD_ID
import com.redgrapefruit.goldenforge.util.getModVersion
import com.redgrapefruit.goldenforge.util.loader
import com.redgrapefruit.goldenforge.util.logger
import net.fabricmc.api.ModInitializer

object GoldenForge : ModInitializer {
    override fun onInitialize() {
        PipeResourceLoader.registerServer(MetalConfigLoader)

        ModBlocks.initialize()
        ModItems.initialize()

        logger.info("Loaded GoldenForge ${loader.getModVersion(MOD_ID)}.")
    }
}
