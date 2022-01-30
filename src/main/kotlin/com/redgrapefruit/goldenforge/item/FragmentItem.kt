package com.redgrapefruit.goldenforge.item

import com.redgrapefruit.datapipe.ResourceHandle
import com.redgrapefruit.goldenforge.core.MetalConfig
import com.redgrapefruit.goldenforge.core.MetalConfigLoader
import com.redgrapefruit.goldenforge.util.*
import com.redgrapefruit.itemnbt3.CustomData
import com.redgrapefruit.itemnbt3.DataClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World

class FragmentItem : Item(sharedItemSettings.maxCount(1)) {
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (world.isClient) return

        FragmentItemComponent.use(stack) {
            if (!rarityInitialized) {
                rarity = MetalRarity.pick()
                rarityInitialized = true
            }

            age++
        }
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        FragmentItemComponent.use(stack) {
            tooltip += TranslatableText(rarity.getTranslationKey()).formatted(rarity.formatting)

            tooltip += LiteralText(
                translate("tooltip_content.goldenforge.age_label")
                .replace("|value|", convertAgeToMinutes(age).toString())
                + if (convertAgeToMinutes(age) > 1) "s" else "")

            tooltip += TranslatableText("tooltip_content.goldenforge.state_label")
                .append(TranslatableText(getTranslationKeyForState()))
                .formatted(if (clean) Formatting.WHITE else Formatting.GRAY)
        }
    }

    private fun convertAgeToMinutes(age: Int): Int {
        var out = age / (20 /* tick length */ * 60 /* minute length */)

        if (out < 0) out = 0

        return out
    }

    companion object {
        /** Gets the [ResourceHandle] for the [MetalConfig] of a fragment in the given [ItemStack] by hacking on the stack's translation key. */
        fun getAssociatedResourceHandle(stack: ItemStack): ResourceHandle<MetalConfig> {
            var name = stack.translationKey
            name = name.replace("item.$ModID.", "")
            name = name.replace("_fragment", "")
            return MetalConfigLoader.handleFor(name)
        }
    }
}

data class FragmentItemComponent(
    var rarity: MetalRarity = MetalRarity.COMMON,
    var rarityInitialized: Boolean = false,
    var age: Int = 0,
    var clean: Boolean = false
) : CustomData {

    override fun getNbtCategory(): String = "FragmentItemComponent"

    override fun readNbt(nbt: NbtCompound) {
        rarity = MetalRarity.valueOf(nbt.getString("Rarity"))
        rarityInitialized = nbt.getBoolean("Rarity Initialized")
        age = nbt.getInt("Age")
        clean = nbt.getBoolean("Clean")
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putString("Rarity", rarity.name)
        nbt.putBoolean("Rarity Initialized", rarityInitialized)
        nbt.putInt("Age", age)
        nbt.putBoolean("Clean", clean)
    }

    fun getTranslationKeyForState(): String {
        val k = if (clean) "clean" else "dirty"
        return "fragment_state.goldenforge.$k"
    }

    companion object {
        fun use(stack: ItemStack, action: FragmentItemComponent.() -> Unit) {
            DataClient.use(::FragmentItemComponent, stack, action)
        }
    }
}

/** The 8 tiers of rarities for fragments. Picked randomly */
enum class MetalRarity(val formatting: Formatting) {
    COMMON(Formatting.WHITE),
    UNCOMMON(Formatting.GRAY),
    RARE(Formatting.BLUE),
    EPIC(Formatting.DARK_PURPLE),
    MYTHIC(Formatting.DARK_RED),
    LEGENDARY(Formatting.YELLOW);

    fun getTranslationKey(): String {
        return "rarity.$ModID.${name.lowercase()}"
    }

    companion object {
        fun pick(): MetalRarity {
            return when (sharedRandom.nextInt(100)) {
                in 0..50 -> COMMON // 50%
                in 51..70 -> UNCOMMON // 20%
                in 71..82 -> RARE // 12%
                in 83..92 -> EPIC // 9%
                in 93..97 -> MYTHIC // 5%
                else -> LEGENDARY // 3%
            }
        }
    }
}
