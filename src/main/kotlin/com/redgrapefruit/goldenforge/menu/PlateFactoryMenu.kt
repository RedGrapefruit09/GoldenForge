package com.redgrapefruit.goldenforge.menu

import com.redgrapefruit.goldenforge.block.PlateFactoryBlock
import com.redgrapefruit.goldenforge.init.ModMenus
import com.redgrapefruit.goldenforge.util.id
import com.redgrapefruit.goldenforge.util.parsedId
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WSprite
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

        // Input slots in a 3x3 pattern
        panel.add(WItemSlot.of(blockInventory, 0), 1, 1)
        panel.add(WItemSlot.of(blockInventory, 1), 2, 1)
        panel.add(WItemSlot.of(blockInventory, 2), 3, 1)
        panel.add(WItemSlot.of(blockInventory, 3), 1, 2)
        panel.add(WItemSlot.of(blockInventory, 4), 2, 2)
        panel.add(WItemSlot.of(blockInventory, 5), 3, 2)
        panel.add(WItemSlot.of(blockInventory, 6), 1, 3)
        panel.add(WItemSlot.of(blockInventory, 7), 2, 3)
        panel.add(WItemSlot.of(blockInventory, 8), 3, 3)

        // Arrow sprite
        panel.add(WSprite("textures/misc/arrow_right.png".id), 5, 2)

        // Output slot
        panel.add(WItemSlot.of(blockInventory, PlateFactoryBlock.Slot_Output), 7, 2)

        // Fuel slot
        panel.add(WItemSlot.of(blockInventory, PlateFactoryBlock.Slot_Fuel), 6, 5)

        // Fuel icon
        panel.add(WSprite("minecraft:textures/item/coal.png".parsedId), 5, 5)

        // Player's inventory
        panel.add(createPlayerInventoryPanel(), 0, 6)

        panel.validate(this)
    }
}
