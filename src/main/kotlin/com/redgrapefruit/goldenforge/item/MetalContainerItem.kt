package com.redgrapefruit.goldenforge.item

import com.redgrapefruit.goldenforge.core.MetalConfigLoader
import com.redgrapefruit.goldenforge.util.IdNull
import com.redgrapefruit.goldenforge.util.ModID
import com.redgrapefruit.goldenforge.util.referencesNull
import com.redgrapefruit.goldenforge.util.sharedItemSettings
import com.redgrapefruit.itemnbt3.CustomData
import com.redgrapefruit.itemnbt3.DataClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.world.World

class MetalContainerItem : Item(sharedItemSettings.maxCount(1)) {
    fun initialize(selfStack: ItemStack, plateStack: ItemStack, metal: Identifier) {
        MetalContainerComponent.use(selfStack) {
            // Init quality
            val plateQuality = PlateItem.getQuality(plateStack)
            quality = plateQuality
            // Init impurities
            val metalConfig = MetalConfigLoader.handleFor(metal.path).getOrThrow()
            impurities = metalConfig.impurityAmounts.randomize() - quality * metalConfig.impurityInfluenceFromQuality
            // Init ID ref
            metalInside = metal
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        MetalContainerComponent.use(stack) {
            if (metalInside.referencesNull()) {
                tooltip += TranslatableText("tooltip_content.goldenforge.empty")
            } else {
                tooltip += TranslatableText("tooltip_content.goldenforge.contains")
                    .append(TranslatableText(metalRefToTranslationKey(metalInside)))

                tooltip += TranslatableText("tooltip_content.goldenforge.quality_label")
                    .append(LiteralText(quality.toString()))

                tooltip += TranslatableText("tooltip_content.goldenforge.impurities_label")
                    .append(LiteralText(impurities.toString()))
            }
        }
    }

    private fun metalRefToTranslationKey(ref: Identifier): String {
        return "metal.$ModID.${ref.path}"
    }
}

data class MetalContainerComponent(
    var quality: Float = 0F,
    var impurities: Float = 0F,
    var metalInside: Identifier = IdNull
) : CustomData {

    override fun getNbtCategory(): String = "MetalContainer"

    override fun readNbt(nbt: NbtCompound) {
        quality = nbt.getFloat("Quality")
        impurities = nbt.getFloat("Impurities")
        metalInside = Identifier.tryParse(nbt.getString("Metal Inside")) ?: IdNull
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putFloat("Quality", quality)
        nbt.putFloat("Impurities", impurities)
        nbt.putString("Metal Inside", metalInside.toString())
    }

    companion object {
        fun use(stack: ItemStack, action: MetalContainerComponent.() -> Unit) {
            DataClient.use(::MetalContainerComponent, stack, action)
        }
    }
}
