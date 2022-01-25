package com.redgrapefruit.goldenforge.init

import com.redgrapefruit.goldenforge.client.menu.FragmentCleanerClientMenu
import com.redgrapefruit.goldenforge.menu.FragmentCleanerMenu
import com.redgrapefruit.goldenforge.util.IClientInitializer
import com.redgrapefruit.goldenforge.util.IInitializer
import com.redgrapefruit.goldenforge.util.id
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

/** Registers common and client menus. */
object ModMenus : IInitializer, IClientInitializer {
    lateinit var FRAGMENT_CLEANER_MENU: ScreenHandlerType<FragmentCleanerMenu>

    override fun initialize() {
        FRAGMENT_CLEANER_MENU = makeType("fragment_cleaner", ::FragmentCleanerMenu)
    }

    override fun initializeClient() {
        ScreenRegistry.register(FRAGMENT_CLEANER_MENU, ::FragmentCleanerClientMenu)
    }

    private fun <T : ScreenHandler> makeType(name: String, factory: ScreenHandlerRegistry.SimpleClientHandlerFactory<T>): ScreenHandlerType<T> {
        return ScreenHandlerRegistry.registerSimple(name.id, factory)
    }
}
