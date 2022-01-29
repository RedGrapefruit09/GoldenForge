package com.redgrapefruit.goldenforge.menu

import com.redgrapefruit.goldenforge.block.MetalFurnaceBlock
import com.redgrapefruit.goldenforge.init.ModMenus
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class MetalFurnaceMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    context: ScreenHandlerContext = ScreenHandlerContext.EMPTY) : SyncedGuiDescription(

    ModMenus.METAL_FURNACE_MENU,
    syncId,
    playerInventory,
    getBlockInventory(context, MetalFurnaceBlock.InventorySize),
    null
    ) {

    init {
        // Set up window/panel
        val panel = WGridPanel()
        setRootPanel(panel)
        panel.setSize(175, 200)
        panel.insets = Insets.ROOT_PANEL

        panel.validate(this)
    }
}
