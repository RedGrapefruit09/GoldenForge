package com.redgrapefruit.goldenforge.block

import com.redgrapefruit.datapipe.ResourceHandle
import com.redgrapefruit.goldenforge.core.MetalConfig
import com.redgrapefruit.goldenforge.core.MetalConfigLoader
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class MetalOreBlock(settings: Settings, name: String) : Block(settings) {
    private val handle: ResourceHandle<MetalConfig> = MetalConfigLoader.handleFor(name)

    override fun afterBreak(
        world: World,
        player: PlayerEntity,
        pos: BlockPos,
        state: BlockState,
        blockEntity: BlockEntity?,
        stack: ItemStack
    ) {
        super.afterBreak(world, player, pos, state, blockEntity, stack)

        handle.ifAvailable {

        }
    }
}
