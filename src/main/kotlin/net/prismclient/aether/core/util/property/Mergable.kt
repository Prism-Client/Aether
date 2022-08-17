package net.prismclient.aether.core.util.property

/**
 * [Mergable] is a single function interface which accepts [T], the class to be merged. The
 * function [merge] expects the paramater other to apply all the non-null fields to this. The
 * new properties should be a deep copy of the old properties.
 *
 * @author sen
 * @since 1.0
 * @see UIProperty
 */
@Suppress("SpellCheckingInspection")
interface Mergable<T> {
    /**
     * Applies the non-null fields of [other] to this. The fields should be a deep copy (if applicable).
     */
    fun merge(other: T?)
}