package com.redgrapefruit.goldenforge.blockentity

import com.redgrapefruit.goldenforge.block.MetalFurnaceBlock
import com.redgrapefruit.goldenforge.core.MetalConfigLoader
import com.redgrapefruit.goldenforge.init.ModBlocks
import com.redgrapefruit.goldenforge.item.MetalContainerComponent
import com.redgrapefruit.goldenforge.item.MetalContainerItem
import com.redgrapefruit.goldenforge.item.PlateItem
import com.redgrapefruit.goldenforge.menu.MetalFurnaceMenu
import com.redgrapefruit.goldenforge.util.*
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class MetalFurnaceBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.METAL_FURNACE_BLOCK_ENTITY, pos, state)
, DefaultedSidedInventory, InventoryProvider, NamedScreenHandlerFactory {

    // NBT data
    private val items: DefaultedList<ItemStack> = DefaultedList.ofSize(MetalFurnaceBlock.InventorySize, ItemStack.EMPTY)
    private var meltTimer: Int = 0

    override fun getItems(): DefaultedList<ItemStack> = items
    override fun markDirty() = Unit
    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory = this

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)

        Inventories.readNbt(nbt, items)
        meltTimer = nbt.getInt("Melt Timer")
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)

        Inventories.writeNbt(nbt, items)
        nbt.putInt("Melt Timer", meltTimer)
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return MetalFurnaceMenu(syncId, inv, ScreenHandlerContext.create(world, pos))
    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey) // retrieve from block
    }

    fun onTick() {
        // I. Validation

        // Input slot
        val inputSlot = items[MetalFurnaceBlock.Slot_Input]
        if (inputSlot.isEmpty) return
        if (inputSlot.item !is PlateItem) return

        // Output slot
        val outputSlot = items[MetalFurnaceBlock.Slot_Output]
        if (outputSlot.isEmpty) return
        if (outputSlot.item !is MetalContainerItem) return
        var emptyContainer = false
        MetalContainerComponent.use(outputSlot) {
            emptyContainer = metalInside.referencesNull()
        }
        if (!emptyContainer) return

        // Fuel slot
        val fuelSlot = items[MetalFurnaceBlock.Slot_Fuel]
        if (fuelSlot.isEmpty) return
        if (!isFuel(fuelSlot)) return

        // II. Process

        meltTimer++
        val metalName = resolveMetalName(inputSlot)
        val metalConfig = MetalConfigLoader.handleFor(metalName).getOrThrow()
        if (meltTimer < metalConfig.furnaceMeltTime) return
        meltTimer = 0

        // III. Reward

        // Consume fuel
        fuelSlot.decrement(1)
        // Fill in container
        val containerItem = outputSlot.item as MetalContainerItem
        containerItem.initialize(outputSlot, inputSlot, metalName.id)
        // Remove plate
        items[MetalFurnaceBlock.Slot_Input] = ItemStack.EMPTY
    }

    private fun resolveMetalName(stack: ItemStack): String {
        return stack.translationKey
            .replace("item.$ModID.", "")
            .replace("_plate", "")
    }

    companion object : BlockEntityTicker<MetalFurnaceBlockEntity> {
        override fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: MetalFurnaceBlockEntity) {
            blockEntity.onTick()
        }
    }
}
