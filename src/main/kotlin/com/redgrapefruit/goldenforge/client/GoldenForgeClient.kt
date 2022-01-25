package com.redgrapefruit.goldenforge.client

import com.redgrapefruit.goldenforge.init.ModMenus
import net.fabricmc.api.ClientModInitializer

object GoldenForgeClient : ClientModInitializer {
    override fun onInitializeClient() {
        ModMenus.initializeClient()
    }
}
