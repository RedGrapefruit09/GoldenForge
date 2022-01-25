package com.redgrapefruit.goldenforge.blockentity

import com.redgrapefruit.goldenforge.block.FragmentCleanerBlock
import com.redgrapefruit.goldenforge.init.ModBlocks
import com.redgrapefruit.goldenforge.menu.FragmentCleanerMenu
import com.redgrapefruit.goldenforge.util.DefaultedSidedInventory
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
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
import net.minecraft.world.WorldAccess

class FragmentCleanerBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.FRAGMENT_CLEANER_BLOCK_ENTITY, pos, state)
, DefaultedSidedInventory, InventoryProvider, NamedScreenHandlerFactory {

    private val items: DefaultedList<ItemStack> = DefaultedList.ofSize(FragmentCleanerBlock.InventorySize, ItemStack.EMPTY)

    override fun getItems(): DefaultedList<ItemStack> = items
    override fun markDirty() = Unit
    override fun getInventory(state: BlockState?, world: WorldAccess?, pos: BlockPos?): SidedInventory = this

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)

        Inventories.readNbt(nbt, items)
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)

        Inventories.writeNbt(nbt, items)
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return FragmentCleanerMenu(syncId, inv, ScreenHandlerContext.create(world, pos))
    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey) // retrieve from block
    }
}
