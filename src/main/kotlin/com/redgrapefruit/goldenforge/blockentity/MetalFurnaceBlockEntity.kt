package com.redgrapefruit.goldenforge.blockentity

import com.redgrapefruit.goldenforge.block.MetalFurnaceBlock
import com.redgrapefruit.goldenforge.init.ModBlocks
import com.redgrapefruit.goldenforge.menu.MetalFurnaceMenu
import com.redgrapefruit.goldenforge.util.DefaultedSidedInventory
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

    override fun getItems(): DefaultedList<ItemStack> = items
    override fun markDirty() = Unit
    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory = this

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)

        Inventories.readNbt(nbt, items)
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)

        Inventories.writeNbt(nbt, items)
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return MetalFurnaceMenu(syncId, inv, ScreenHandlerContext.create(world, pos))
    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey) // retrieve from block
    }

    fun onTick() {
        // more logic later
    }

    companion object : BlockEntityTicker<MetalFurnaceBlockEntity> {
        override fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: MetalFurnaceBlockEntity) {
            blockEntity.onTick()
        }
    }
}
