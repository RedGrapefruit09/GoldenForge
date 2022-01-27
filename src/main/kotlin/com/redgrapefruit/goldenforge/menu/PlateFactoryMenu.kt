package com.redgrapefruit.goldenforge.menu

import com.redgrapefruit.goldenforge.block.PlateFactoryBlock
import com.redgrapefruit.goldenforge.init.ModMenus
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class PlateFactoryMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    context: ScreenHandlerContext = ScreenHandlerContext.EMPTY) : SyncedGuiDescription(

    ModMenus.PLATE_FACTORY_MENU,
    syncId,
    playerInventory,
    getBlockInventory(context, PlateFactoryBlock.InventorySize),
    null
    ) {

    init {
        // Set up window/panel
        val panel = WGridPanel()
        setRootPanel(panel)
        panel.setSize(175, 200)
        panel.insets = Insets.ROOT_PANEL

        // Player's inventory
        panel.add(createPlayerInventoryPanel(), 0, 6)

        panel.validate(this)
    }
}
