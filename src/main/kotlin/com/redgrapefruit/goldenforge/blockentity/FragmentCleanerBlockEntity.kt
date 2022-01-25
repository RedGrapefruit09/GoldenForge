package com.redgrapefruit.goldenforge.blockentity

import com.redgrapefruit.goldenforge.block.FragmentCleanerBlock
import com.redgrapefruit.goldenforge.init.ModBlocks
import com.redgrapefruit.goldenforge.init.ModItems
import com.redgrapefruit.goldenforge.item.FragmentItem
import com.redgrapefruit.goldenforge.item.FragmentItemComponent
import com.redgrapefruit.goldenforge.item.MetalRarity
import com.redgrapefruit.goldenforge.menu.FragmentCleanerMenu
import com.redgrapefruit.goldenforge.util.DefaultedSidedInventory
import com.redgrapefruit.goldenforge.util.applyChance
import com.redgrapefruit.goldenforge.util.isFuel
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

class FragmentCleanerBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.FRAGMENT_CLEANER_BLOCK_ENTITY, pos, state)
, DefaultedSidedInventory, InventoryProvider, NamedScreenHandlerFactory {

    // NBT data
    private val items: DefaultedList<ItemStack> = DefaultedList.ofSize(FragmentCleanerBlock.InventorySize, ItemStack.EMPTY)
    private var processTimer: Int = 0

    override fun getItems(): DefaultedList<ItemStack> = items
    override fun markDirty() = Unit
    override fun getInventory(state: BlockState?, world: WorldAccess?, pos: BlockPos?): SidedInventory = this

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)

        Inventories.readNbt(nbt, items)
        processTimer = nbt.getInt("Process Timer")
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)

        Inventories.writeNbt(nbt, items)
        nbt.putInt("Process Timer", processTimer)
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return FragmentCleanerMenu(syncId, inv, ScreenHandlerContext.create(world, pos))
    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey) // retrieve from block
    }

    fun onTick() {
        // I. Checks

        // Input slot
        val inputSlot = items[FragmentCleanerBlock.Slot_Input] // is the input a fragment?
        if (inputSlot.item !is FragmentItem) return
        var cleanInput = false // does the input need cleaning?
        FragmentItemComponent.use(inputSlot) {
            if (clean) cleanInput = true
        }
        if (cleanInput) return

        // Output slot
        val outputSlot = items[FragmentCleanerBlock.Slot_Output]
        if (!outputSlot.isEmpty) return // is the output slot empty?

        // Fuel slot
        val fuelSlot = items[FragmentCleanerBlock.Slot_Fuel]
        if (fuelSlot.isEmpty) return // is there no fuel?
        if (!isFuel(fuelSlot)) return // is the fuel invalid?

        // Rubbish slot
        val rubbishSlot = items[FragmentCleanerBlock.Slot_Rubbish]
        if (!rubbishSlot.isEmpty) { // it's obviously optional to put in rubbish
            if (!rubbishSlot.translationKey.endsWith("rubbish")) return // is the rubbish some invalid item?
        }

        // II. Process

        // Retrieve the required time by querying the input fragment's resource, in which it's stored as a field
        val inputFragmentHandle = FragmentItem.getAssociatedResourceHandle(inputSlot)
        inputFragmentHandle.ifUnavailable { return } // ticking might happen too early, it's better to check
        val config = inputFragmentHandle.getOrThrow()
        val requiredTime = config.cleaningTime

        // Increment the timer and proceed only if the process is finally over
        processTimer++
        if (processTimer < requiredTime) return
        processTimer = 0 // prepare for attempting to relaunch the process

        // III. Reward

        // Failure outcome
        var inputRarity: MetalRarity? = null
        FragmentItemComponent.use(inputSlot) {
            inputRarity = rarity
        }
        applyChance(config.processFailureChances[inputRarity]!!) {
            // Empty both input and output slots
            items[FragmentCleanerBlock.Slot_Input] = ItemStack.EMPTY
            items[FragmentCleanerBlock.Slot_Output] = ItemStack.EMPTY
            // Increment/create rubbish
            if (rubbishSlot.isEmpty) {
                items[FragmentCleanerBlock.Slot_Rubbish] = ItemStack(ModItems.RUBBISH)
            } else {
                rubbishSlot.increment(1)
            }
            // Use up fuel
            fuelSlot.decrement(1)
            return
        }


        // Successful outcome
        // Move input item to output slot
        items[FragmentCleanerBlock.Slot_Output] = items[FragmentCleanerBlock.Slot_Input]
        items[FragmentCleanerBlock.Slot_Input] = ItemStack.EMPTY
        // Mark the output as clean
        FragmentItemComponent.use(items[FragmentCleanerBlock.Slot_Output]) {
            clean = true
        }
        // Consume fuel
        fuelSlot.decrement(1)
    }

    companion object : BlockEntityTicker<FragmentCleanerBlockEntity> { // wrap the companion as a ticker impl
        override fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: FragmentCleanerBlockEntity) {
            blockEntity.onTick()
        }
    }
}
