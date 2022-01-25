package com.redgrapefruit.goldenforge.menu

import com.redgrapefruit.goldenforge.block.FragmentCleanerBlock
import com.redgrapefruit.goldenforge.init.ModMenus
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class FragmentCleanerMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    context: ScreenHandlerContext = ScreenHandlerContext.EMPTY) : SyncedGuiDescription(

    ModMenus.FRAGMENT_CLEANER_MENU,
    syncId,
    playerInventory,
    getBlockInventory(context, FragmentCleanerBlock.InventorySize),
    null) {

    init {
        val panel = WGridPanel()
        setRootPanel(panel)
        panel.setSize(300, 200)
        panel.insets = Insets.ROOT_PANEL

        panel.validate(this)
    }
}
