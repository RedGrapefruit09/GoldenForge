package com.redgrapefruit.goldenforge.item

import com.redgrapefruit.goldenforge.util.Constants
import com.redgrapefruit.goldenforge.util.ensurePositive
import com.redgrapefruit.goldenforge.util.sharedItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World
import java.util.*

class PlateItem : Item(sharedItemSettings.maxCount(1)) {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip += TranslatableText("tooltip_content.goldenforge.quality_label")
            .append(LiteralText(getQuality(stack).toString() + "Q" /* currency mark */))
    }

    fun initQuality(fragmentStacks: List<ItemStack>, selfStack: ItemStack) {
        if (getQuality(selfStack) == 0F) return

        selfStack.orCreateNbt.putFloat("Plate Quality", calculatePlateQuality(fragmentStacks))
    }

    override fun getTooltipData(stack: ItemStack?): Optional<TooltipData> {
        return super.getTooltipData(stack)
    }

    companion object {
        // creating an ItemNBT CustomData here would be overkill for a single field
        fun getQuality(stack: ItemStack): Float {
            return if (stack.hasNbt()) {
                stack.nbt!!.getFloat("Plate Quality")
            } else {
                stack.nbt = NbtCompound().apply { putFloat("Plate Quality", 0F) }
                0F
            }
        }

        private fun calculatePlateQuality(stacks: List<ItemStack>): Float {
            var quality = 0F

            // there are two factors impacting plate quality (and then molten quality):
            // - summed rarity (positively)
            // - summed age (negatively; cannot be higher than zero)

            stacks.forEach { stack ->
                FragmentItemComponent.use(stack) {
                    quality +=
                        rarityToQuality(rarity) -
                        ensurePositive(age / Constants.MINUTE_LENGTH_IN_TICKS / Constants.TICK_TO_QUALITY_DIVISOR)
                }
            }

            return quality
        }

        private fun rarityToQuality(rarity: MetalRarity): Float {
            return when (rarity) {
                MetalRarity.COMMON -> 1.0F
                MetalRarity.UNCOMMON -> 1.5F
                MetalRarity.RARE -> 2.0F
                MetalRarity.EPIC -> 3.0F
                MetalRarity.MYTHIC -> 3.75F
                MetalRarity.LEGENDARY -> 4.8F
            }
        }
    }
}
