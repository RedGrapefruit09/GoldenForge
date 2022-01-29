package com.redgrapefruit.goldenforge.client.menu

import com.redgrapefruit.goldenforge.menu.FragmentCleanerMenu
import com.redgrapefruit.goldenforge.menu.MetalFurnaceMenu
import com.redgrapefruit.goldenforge.menu.PlateFactoryMenu
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class FragmentCleanerClientMenu(commonMenu: FragmentCleanerMenu, playerInventory: PlayerInventory, title: Text)
    : CottonInventoryScreen<FragmentCleanerMenu>(commonMenu, playerInventory, title)

class PlateFactoryClientMenu(commonMenu: PlateFactoryMenu, playerInventory: PlayerInventory, title: Text)
    : CottonInventoryScreen<PlateFactoryMenu>(commonMenu, playerInventory, title)

class MetalFurnaceClientMenu(commonMenu: MetalFurnaceMenu, playerInventory: PlayerInventory, title: Text)
    : CottonInventoryScreen<MetalFurnaceMenu>(commonMenu, playerInventory, title)
