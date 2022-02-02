package com.redgrapefruit.goldenforge.menu

import com.redgrapefruit.goldenforge.block.FragmentCleanerBlock
import com.redgrapefruit.goldenforge.block.MetalFurnaceBlock
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

        // Input slot
        panel.add(WItemSlot.of(blockInventory, MetalFurnaceBlock.Slot_Input), 4, 1)
        // Output slot
        panel.add(WItemSlot.of(blockInventory, MetalFurnaceBlock.Slot_Output), 4, 5)
        // Input => output arrow
        panel.add(WSprite("textures/misc/arrow.png".id), 4, 3)
        // Fuel slot
        panel.add(WItemSlot.of(blockInventory, MetalFurnaceBlock.Slot_Fuel), 1, 1)
        // Fuel icon
        panel.add(WSprite("minecraft:textures/item/coal.png".parsedId), 1, 2)

        panel.add(createPlayerInventoryPanel(), 0, 6)

        panel.validate(this)
    }
}
