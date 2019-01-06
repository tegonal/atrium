package ch.tutteli.atrium.api.cc.infix.en_GB

import ch.tutteli.atrium.api.cc.infix.en_GB.keywords.Empty
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.verbs.internal.AssertionVerbFactory
import kotlin.reflect.KFunction2

class MapAssertionsSpec : ch.tutteli.atrium.spec.integration.MapAssertionsSpec(
    AssertionVerbFactory,
    containsFun.name to Companion::contains,
    containsNullableFun.name to Companion::containsNullable,
    Assert<Map<String, Int>>::containsKey.name to Companion::containsKey,
    "${Assert<Map<String?, *>>::containsKey.name} for nullable" to Companion::containsNullableKey,
    Assert<Map<*, *>>::hasSize.name to Companion::hasSize,
    "${Assert<Map<*, *>>::toBe.name} ${Empty::class.simpleName}" to Companion::isEmpty,
    "${Assert<Map<*, *>>::notToBe.name} ${Empty::class.simpleName}" to Companion::isNotEmpty
) {
    companion object {
        private val containsFun : KFunction2<Assert<Map<String, Int>>, Pair<String, Int>,Assert<Map<String, Int>>> = Assert<Map<String, Int>>::contains
        private fun contains(plant: Assert<Map<String, Int>>, pair: Pair<String, Int>, otherPairs: Array<out Pair<String, Int>>): Assert<Map<String, Int>> {
            return if (otherPairs.isEmpty()) {
                plant contains pair
            } else {
                plant contains Pairs(pair, *otherPairs)
            }
        }

        private val containsNullableFun : KFunction2<Assert<Map<String?, Int?>>, Pair<String?, Int?>,Assert<Map<String?, Int?>>> = Assert<Map<String?, Int?>>::containsNullable
        private fun containsNullable(plant: Assert<Map<String?, Int?>>, pair: Pair<String?, Int?>, otherPairs: Array<out Pair<String?, Int?>>): Assert<Map<String?, Int?>> {
            return if (otherPairs.isEmpty()) {
                plant containsNullable pair
            } else {
                plant containsNullable Pairs(pair, *otherPairs)
            }
        }

        private fun containsKey(plant: Assert<Map<String, *>>, key: String)
            = plant containsKey key

        private fun containsNullableKey(plant: Assert<Map<String?, *>>, key: String?)
            = plant containsKey key

        private fun hasSize(plant: Assert<Map<*, *>>, size: Int)
            = plant hasSize size

        private fun isEmpty(plant: Assert<Map<*, *>>)
            = plant toBe Empty

        private fun isNotEmpty(plant: Assert<Map<*, *>>)
            = plant notToBe Empty
    }
}

