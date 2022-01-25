package com.redgrapefruit.goldenforge.menu

import com.redgrapefruit.goldenforge.block.FragmentCleanerBlock
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
        // Set up window/panel
        val panel = WGridPanel()
        setRootPanel(panel)
        panel.setSize(175, 200)
        panel.insets = Insets.ROOT_PANEL

        // Input slot
        panel.add(WItemSlot.of(blockInventory, FragmentCleanerBlock.Slot_Input), 4, 1)
        // Output slot
        panel.add(WItemSlot.of(blockInventory, FragmentCleanerBlock.Slot_Output), 4, 5)
        // Arrow pointing down from the input slot to the output slot
        panel.add(WSprite("textures/misc/arrow.png".id), 4, 3)
        // Fuel slot
        panel.add(WItemSlot.of(blockInventory, FragmentCleanerBlock.Slot_Fuel), 1, 3)
        // Fuel icon
        panel.add(WSprite("minecraft:textures/item/coal.png".parsedId), 1, 4)
        // Rubbish slot
        panel.add(WItemSlot.of(blockInventory, FragmentCleanerBlock.Slot_Rubbish), 7, 3)
        // Rubbish icon
        panel.add(WSprite("textures/item/rubbish.png".id), 7, 4)

        // Player's inventory
        panel.add(createPlayerInventoryPanel(), 0, 6)

        panel.validate(this)
    }
}
