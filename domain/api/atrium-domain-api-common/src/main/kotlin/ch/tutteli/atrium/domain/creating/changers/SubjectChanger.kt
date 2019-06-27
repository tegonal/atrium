package ch.tutteli.atrium.domain.creating.changers

import ch.tutteli.atrium.core.polyfills.loadSingleService
import ch.tutteli.atrium.creating.Expect
import ch.tutteli.atrium.reporting.translating.Translatable

/**
 * The access point to an implementation of [SubjectChanger].
 *
 * It loads the implementation lazily via [loadSingleService].
 */
val subjectChanger by lazy { loadSingleService(SubjectChanger::class) }

/**
 * Defines the contract to change the subject of an assertion container (e.g. the subject of [Expect]) by creating
 * a new [Expect] whereas the new [Expect] delegates assertion checking to a given original assertion container.
 */
interface SubjectChanger {

    /**
     * Changes to the subject the given [subjectProvider] provides without showing it in reporting and returns a new
     * [Expect] for it.
     *
     * Explained a bit more in depth: it creates a new [Expect] based on the given [subjectProvider]
     * whereas the new [Expect] delegates assertion checking to the given [originalAssertionContainer] -
     * the change as such will not be reflected in reporting.
     *
     * This method is useful if you want to make feature assertion(s) but you do not want that the feature is shown up
     * in reporting. For instance, if a class can behave as another class (e.g. `Sequence::asIterable`) or you want to
     * hide a conversion (e.g. `Int::toChar`) then you can use this function.
     *
     * Notice, in case the change to the new subject is not always safe (you assert so but it does not have to be),
     * then you should use [reported] so that the assertion is reflected in reporting.
     *
     * @param originalAssertionContainer the assertion container with the current subject (before the change) --
     *   if you use `ExpectImpl.changeSubject.unreported(...)` within an assertion function (an extension function of
     *   [Expect]) then this is usually `this` (so the instance of [Expect]).
     * @param subjectProvider Provides the subject.
     *
     * @return the newly created [Expect].
     */
    fun <T, R> unreported(
        originalAssertionContainer: Expect<T>,
        subjectProvider: () -> R
    ): Expect<R>


    /**
     * Changes to the subject the given [subjectProvider] provides but only if the current subject [canBeTransformed]
     * to the new subject -- the change as such is reflected in reporting by the given
     * [description] and [representation].
     *
     * Explained a bit more in depth: it creates a new [Expect] based on the given [subjectProvider]
     * whereas the new [Expect] delegates assertion checking to the given [originalAssertionContainer].
     *
     * This method is useful if you want to change the subject whereas the change as such is assertion like as well, so
     * that it should be reported as well. For instance, say you want to change the subject of type [Collection] to the
     * [Collection.first] element. Since the collection could also be empty it makes sense to report this assertion
     * instead of failing.
     *
     * @param originalAssertionContainer the assertion container with the current subject (before the change) --
     *   if you use `ExpectImpl.changeSubject.unreported(...)` within an assertion function (an extension function of
     *   [Expect]) then this is usually `this` (so the instance of [Expect]).
     * @param description Describes the kind of subject change (e.g. in case of a type change `is a`).
     * @param representation Representation of the change (e.g. in case of a type transformation the KClass).
     * @param canBeTransformed Indicates whether it is safe to transform to the new subject.
     * @param subjectProvider Provides the subject.
     * @param subAssertions Optionally, subsequent assertions for the new subject. This is especially useful if the
     *   change fails since we can then already show to the user what we wanted to assert.
     *
     * @return the newly created [Expect].
     */
    fun <T, R> reported(
        originalAssertionContainer: Expect<T>,
        description: Translatable,
        representation: Any,
        canBeTransformed: (T) -> Boolean,
        subjectProvider: () -> R,
        subAssertions: (Expect<R>.() -> Unit)?
    ): Expect<R>
}
