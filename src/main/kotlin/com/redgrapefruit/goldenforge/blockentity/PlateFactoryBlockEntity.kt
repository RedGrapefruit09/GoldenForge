package com.redgrapefruit.goldenforge.blockentity

import com.redgrapefruit.goldenforge.block.PlateFactoryBlock
import com.redgrapefruit.goldenforge.init.ModBlocks
import com.redgrapefruit.goldenforge.item.FragmentItem
import com.redgrapefruit.goldenforge.item.FragmentItemComponent
import com.redgrapefruit.goldenforge.item.PlateItem
import com.redgrapefruit.goldenforge.menu.PlateFactoryMenu
import com.redgrapefruit.goldenforge.util.DefaultedSidedInventory
import com.redgrapefruit.goldenforge.util.isFuel
import com.redgrapefruit.goldenforge.util.parsedId
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
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class PlateFactoryBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.PLATE_FACTORY_BLOCK_ENTITY, pos, state)
, DefaultedSidedInventory, InventoryProvider, NamedScreenHandlerFactory {

    // NBT data
    private val items: DefaultedList<ItemStack> = DefaultedList.ofSize(PlateFactoryBlock.InventorySize, ItemStack.EMPTY)
    private var processTimer: Int = 0

    override fun getItems(): DefaultedList<ItemStack> = items
    override fun markDirty() = Unit
    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory = this

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
        return PlateFactoryMenu(syncId, inv, ScreenHandlerContext.create(world, pos))
    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey) // retrieve from block
    }

    fun onTick() {
        // I. Checks

        // Input slots
        var invalidInputs = false
        val signatures = mutableListOf<String>() // are all fragments of the same type?
        PlateFactoryBlock.Slots_Input.forEach { idx ->
            val stack = items[idx]
            if (stack.item is FragmentItem) { // are inputs all fragments?
                FragmentItemComponent.use(stack) {
                    if (!clean) { // are all fragments clean?
                        invalidInputs = true
                    } else {
                        signatures += stack.translationKey
                    }
                    signatures += stack.translationKey
                }
            } else {
                invalidInputs = true
            }
        }
        if (invalidInputs) return
        if (signatures.distinct().size > 1) return // verify "signatures" (translation keys) to be all equal

        // Output slot
        val outputSlot = items[PlateFactoryBlock.Slot_Output]
        if (!outputSlot.isEmpty) return // is the output slot free?

        // Fuel slot
        val fuelSlot = items[PlateFactoryBlock.Slot_Fuel]
        if (fuelSlot.isEmpty) return // is there any fuel inserted?
        if (!isFuel(fuelSlot)) return // is the inserted fuel valid?

        // II. Process

        // Retrieve required time for the process by querying the input config

        // with the .distinct() check above, it's guaranteed all fragments are the same, so this won't break anything
        val inputFragmentHandle = FragmentItem.getAssociatedResourceHandle(items[PlateFactoryBlock.Slots_Input.first()])
        inputFragmentHandle.ifUnavailable { return }
        val config = inputFragmentHandle.getOrThrow()
        val requiredTime = config.plateCreationTime

        // Increment the timer and only proceed at the end of the process
        processTimer++
        if (processTimer < requiredTime) return
        processTimer = 0

        // III. Reward (always successful)

        // Generate and assign output
        val plateItem = Registry.ITEM.get(config.plateId.parsedId) as PlateItem
        val plateStack = ItemStack(plateItem)
        items[PlateFactoryBlock.Slot_Output] = plateStack
        val inputStackList = mutableListOf<ItemStack>()
        PlateFactoryBlock.Slots_Input.forEach { idx -> inputStackList += items[idx] }
        plateItem.initQuality(inputStackList, plateStack)

        // Consume all inputs
        PlateFactoryBlock.Slots_Input.forEach { idx -> items[idx] = ItemStack.EMPTY }

        // Consume fuel
        fuelSlot.decrement(1)
    }

    companion object : BlockEntityTicker<PlateFactoryBlockEntity> {
        override fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: PlateFactoryBlockEntity) {
            blockEntity.onTick()
        }
    }
}
