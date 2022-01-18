package com.redgrapefruit.goldenforge.core

import com.redgrapefruit.datapipe.JsonResourceLoader
import com.redgrapefruit.datapipe.Pipeline
import com.redgrapefruit.datapipe.ResourceHandle
import com.redgrapefruit.goldenforge.util.MOD_ID
import com.redgrapefruit.goldenforge.util.id
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.random.Random

/** A shared JSON configuration for a type of metal. */
@Serializable
data class MetalConfig(
    /** All configured ore drops for this metal. */
    val drops: List<MetalOreDrop>
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
) /*: KSerializer<Range>*/ {

    /*
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Range", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Range {
        // Decode value from string
        val raw = decoder.decodeString()
        // If the value isn't formatted like "x-y" (a range), it's a fixed integer represented by the FixedRange type
        if (!raw.contains('-')) return FixedRange(raw.toInt())
        // Parse the two parts of the format (first is min, second is max)
        val parts = raw.split("-")
        if (parts.size != 2) throw RuntimeException("Failed to parse range from \"$raw\". Invalid format!")
        // Create resulting range
        return Range(parts[0].toInt(), parts[1].toInt())
    }

    override fun serialize(encoder: Encoder, value: Range) {
        // Serialize as a fixed integer-string for fixed values
        if (this is FixedRange) {
            encoder.encodeString(fixedValue.toString())
            return
        }
        // Else, serialize in the "x-y" format
        encoder.encodeString("$min-$max")
    }
     */

    /** Pick a random (or fixed, in case of [FixedRange]) value from this range */
    open fun randomize(): Int {
        return Random.nextInt(min, max + 1)
    }
}

/*
/** A separately handled subset of a [Range], which isn't random, but fixed. */
class FixedRange(val fixedValue: Int) : Range(0, 0) {
    override fun randomize(): Int = fixedValue
}
 */

private val pipeline = Pipeline // pipeline for MetalConfigLoader
    .builder<MetalConfig>()
    .underId("metal_config_loader".id)
    .storedIn("metal_config")
    .filterByExtension(".json")
    .build()

/** A [JsonResourceLoader] for [MetalConfig]s, providing utilities and storing the [Pipeline]. */
object MetalConfigLoader : JsonResourceLoader<MetalConfig>(MOD_ID, MetalConfig.serializer(), pipeline) {
    fun handleFor(name: String): ResourceHandle<MetalConfig> {
        return com.redgrapefruit.goldenforge.core.pipeline.resource(name.id)
    }
}
