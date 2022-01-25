package com.redgrapefruit.goldenforge.core

import com.redgrapefruit.datapipe.JsonResourceLoader
import com.redgrapefruit.datapipe.Pipeline
import com.redgrapefruit.datapipe.ResourceHandle
import com.redgrapefruit.goldenforge.item.MetalRarity
import com.redgrapefruit.goldenforge.util.ModID
import com.redgrapefruit.goldenforge.util.id
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.random.Random

/** A shared JSON configuration for a type of metal. */
@Serializable
data class MetalConfig(
    /** All configured ore drops for this metal. */
    val drops: List<MetalOreDrop>,
    /** The exact amount of in-game ticks required for the fragment cleaning process to finish. */
    val cleaningTime: Int,
    /** Every percentage chance of failure depending on rarity of the metal */
    val processFailureChances: Map<MetalRarity, Int>
)

/** A component of [MetalConfig] that stores data about items dropped by breaking the metal's ore. */
@Serializable
data class MetalOreDrop(
    /** The ID form of the dropped item, for example, `minecraft:shears` */
    val item: String,
    /** The _percentage_ chance of the drop */
    val chance: Int,
    /** The fixed/random amount of the item that should be dropped */
    val amount: Range
)

/**
 * A range of two possible types:
 *
 * - [min] to [max]. Picked randomly. Implemented by this base class.
 * - A simple integer. Not random, but a fixed constant. Implemented by [FixedRange].
 *
 * The [min]-to-[max] is serialized as "x-y", where x is min, and y is max.
 * The fixed-value range is serialized as "z", where z is the constant value.
 *
 * All of this is accounted for in the [KSerializer] implementation.
 *
 * To pick from this range (fixed or random), use the [randomize] method.
 */
@Serializable
open class Range(
    /** Minimal value in this range */
    val min: Int,
    /** Maximal value in this range */
    val max: Int,
) {
    /** Pick a random (or fixed, in case of [FixedRange]) value from this range */
    open fun randomize(): Int {
        return Random.nextInt(min, max + 1)
    }
}

private val pipeline = Pipeline // pipeline for MetalConfigLoader
    .builder<MetalConfig>()
    .underId("metal_config_loader".id)
    .storedIn("metal_config")
    .filterByExtension(".json")
    .build()

/** A [JsonResourceLoader] for [MetalConfig]s, providing utilities and storing the [Pipeline]. */
object MetalConfigLoader : JsonResourceLoader<MetalConfig>(ModID, MetalConfig.serializer(), pipeline) {
    fun handleFor(name: String): ResourceHandle<MetalConfig> {
        return pipeline.resource(name.id)
    }
}
