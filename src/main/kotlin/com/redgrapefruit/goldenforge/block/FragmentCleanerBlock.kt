package com.redgrapefruit.goldenforge.block

import com.redgrapefruit.goldenforge.blockentity.FragmentCleanerBlockEntity
import com.redgrapefruit.goldenforge.util.isServer
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class FragmentCleanerBlock(settings: Settings) : Block(settings), BlockEntityProvider {
    companion object {
        // 1. Input slot
        // 2. Output slot
        // 3. Fuel slot
        // 4. Rubbish slot
        const val InventorySize = 4
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return FragmentCleanerBlockEntity(pos, state)
    }

    private lateinit var facing: DirectionProperty

    init {
        defaultState = stateManager.defaultState.with(facing, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)

        facing = Properties.HORIZONTAL_FACING
        builder.add(facing)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(facing, rotation.rotate(state.get(facing)))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state.get(facing)))

    override fun getPlacementState(context: ItemPlacementContext): BlockState =
        defaultState.with(facing, context.playerFacing.opposite)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ActionResult {
        if (world.isServer) {
            val factory = state.createScreenHandlerFactory(world, pos)
            player.openHandledScreen(factory)
        }

        return ActionResult.SUCCESS
    }

    override fun createScreenHandlerFactory(
        state: BlockState,
        world: World,
        pos: BlockPos,
    ): NamedScreenHandlerFactory? {
        val blockEntity = world.getBlockEntity(pos)
        return if (blockEntity is NamedScreenHandlerFactory) blockEntity else null
    }
}
