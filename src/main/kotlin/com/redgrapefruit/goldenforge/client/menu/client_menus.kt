package com.redgrapefruit.goldenforge.client.menu

import com.redgrapefruit.goldenforge.menu.FragmentCleanerMenu
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class FragmentCleanerClientMenu(commonMenu: FragmentCleanerMenu, playerInventory: PlayerInventory, title: Text)
    : CottonInventoryScreen<FragmentCleanerMenu>(commonMenu, playerInventory, title)
