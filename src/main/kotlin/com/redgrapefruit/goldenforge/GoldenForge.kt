package com.redgrapefruit.goldenforge

import com.redgrapefruit.goldenforge.util.MOD_ID
import com.redgrapefruit.goldenforge.util.getModVersion
import com.redgrapefruit.goldenforge.util.loader
import net.fabricmc.api.ModInitializer

object GoldenForge : ModInitializer {
    override fun onInitialize() {
        println("Loaded GoldenForge ${loader.getModVersion(MOD_ID)}.")
    }
}
