package com.redgrapefruit.goldenforge.item

import com.redgrapefruit.goldenforge.util.MOD_ID
import com.redgrapefruit.goldenforge.util.sharedItemSettings
import com.redgrapefruit.itemnbt3.CustomData
import com.redgrapefruit.itemnbt3.DataClient
import com.redgrapefruit.itemnbt3.linking.DataLink
import com.redgrapefruit.itemnbt3.specification.Specification
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World
import kotlin.random.Random

class FragmentItem : Item(sharedItemSettings) {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        FragmentItemComponent.use(stack) {
            tooltip += TranslatableText(rarity.getTranslationKey()).formatted(rarity.formatting)
        }
    }
}

data class FragmentItemComponent(
    var rarity: FragmentRarity = FragmentRarity.NULL
) : CustomData {

    override fun getNbtCategory(): String = "FragmentItemComponent"

    override fun readNbt(nbt: NbtCompound) {
        rarity = FragmentRarity.valueOf(nbt.getString("Rarity"))
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putString("Rarity", rarity.name)
    }

    companion object {
        private val spec = Specification.create(FragmentItemComponent::class.java)
        private val link = DataLink.create(FragmentItemComponent::class.java)

        fun use(stack: ItemStack, action: FragmentItemComponent.() -> Unit) {
            DataClient.use(stack, spec, link, FragmentItemComponent(), action)
        }
    }
}

/** The 8 tiers of rarities for fragments. Picked randomly */
enum class FragmentRarity(val formatting: Formatting) {
    NULL(Formatting.WHITE), /** placeholder value */

    DAMAGED(Formatting.DARK_RED),
    COMMON(Formatting.WHITE),
    UNCOMMON(Formatting.DARK_GRAY),
    RARE(Formatting.BLUE),
    EPIC(Formatting.DARK_PURPLE),
    MYTHIC(Formatting.DARK_RED),
    LEGENDARY(Formatting.YELLOW),
    GODLY(Formatting.OBFUSCATED);

    fun getTranslationKey(): String {
        return "rarity.$MOD_ID.${name.lowercase()}"
    }

    companion object {
        fun pick(): FragmentRarity {
            val k = Random.nextInt(1, 1001) // base random value

            // chance values noted below
            return when {
                k.between(1, 75) -> DAMAGED // 7.5%
                k.between(76, 650) -> COMMON // 57.5%
                k.between(651, 800) -> UNCOMMON // 15%
                k.between(801, 875) -> RARE // 7.5%
                k.between(876, 925) -> EPIC // 5%
                k.between(926, 960) -> MYTHIC // 3.5%
                k.between(961, 985) -> LEGENDARY // 2.5%
                else -> GODLY // 1.5%
            }
        }
    }
}

private fun Int.between(a: Int, b: Int): Boolean {
    return this in a..b
}
