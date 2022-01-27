package com.redgrapefruit.goldenforge.init

import com.redgrapefruit.goldenforge.client.menu.FragmentCleanerClientMenu
import com.redgrapefruit.goldenforge.client.menu.PlateFactoryClientMenu
import com.redgrapefruit.goldenforge.menu.FragmentCleanerMenu
import com.redgrapefruit.goldenforge.menu.PlateFactoryMenu
import com.redgrapefruit.goldenforge.util.IInitializer
import com.redgrapefruit.goldenforge.util.id
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

/** Registers common and client menus. */
object ModMenus : IInitializer {
    lateinit var FRAGMENT_CLEANER_MENU: ScreenHandlerType<FragmentCleanerMenu>
    lateinit var PLATE_FACTORY_MENU: ScreenHandlerType<PlateFactoryMenu>

    override fun initialize() {
        FRAGMENT_CLEANER_MENU = makeType("fragment_cleaner", ::FragmentCleanerMenu)
        PLATE_FACTORY_MENU = makeType("plate_factory", ::PlateFactoryMenu)
    }

    @Environment(EnvType.CLIENT) // avoid making a separate class for the client impl with this
    fun initializeClient() {
        ScreenRegistry.register(FRAGMENT_CLEANER_MENU, ::FragmentCleanerClientMenu)
        ScreenRegistry.register(PLATE_FACTORY_MENU, ::PlateFactoryClientMenu)
    }

    private fun <T : ScreenHandler> makeType(name: String, factory: ScreenHandlerRegistry.SimpleClientHandlerFactory<T>): ScreenHandlerType<T> {
        return ScreenHandlerRegistry.registerSimple(name.id, factory)
    }
}
