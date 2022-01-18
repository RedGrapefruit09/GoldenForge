package com.redgrapefruit.goldenforge.block

import com.redgrapefruit.datapipe.ResourceHandle
import com.redgrapefruit.goldenforge.core.MetalConfig
import com.redgrapefruit.goldenforge.core.MetalConfigLoader
import com.redgrapefruit.goldenforge.util.applyChance
import com.redgrapefruit.goldenforge.util.parsedId
import com.redgrapefruit.goldenforge.util.translate
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import net.minecraft.world.World

/** Handles item-dropping functionality and displays the relevant settings in its tooltip. */
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

        if (!handle.isAvailable()) return
        val config = handle.getOrThrow()

        config.drops.forEach { drop ->
            applyChance(drop.chance) { // account for the chance
                val item = Registry.ITEM.get(drop.item.parsedId) // query the item registry to find the specified item instance
                val amount = drop.amount.randomize() // obtain the exact dropped amount
                val entity = ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(item, amount))
                world.spawnEntity(entity) // spawn the item in the world
            }
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: BlockView?,
        tooltip: MutableList<Text>,
        options: TooltipContext
    ) {
        if (!handle.isAvailable()) return
        val config = handle.getOrThrow()

        tooltip += TranslatableText("tooltip_content.goldenforge.ore_drops_header")
            .formatted(Formatting.GRAY) // heading for all drops

        // Display the details about every ore drop
        config.drops.forEach { drop ->
            val item = Registry.ITEM.get(drop.item.parsedId) // queue item from registry

            tooltip += LiteralText( // display the translation, replacing the placeholders with actual data
                translate("tooltip_content.goldenforge.ore_drop_label")
                .replace("|translation|", translate(item.translationKey))
                .replace("|chance|", drop.chance.toString())
                .replace("|min|", drop.amount.min.toString())
                .replace("|max|", drop.amount.max.toString()))
        }
    }
}
