package com.timeular.nytta.prova.hamkrest

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.anything
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ContainsInAnyOrderTest {

    @Test
    fun emptyIterable() {
        assertThat(emptyList<Int>(), containsInAnyOrder(emptyList()))
        assertThat(emptyList<Int>(), !containsInAnyOrder(anything))
        assertThat(listOf(anything), !containsInAnyOrder(emptyList<Any>()))
    }

    @Test
    fun matchingIterables() {
        assertThat(listOf(1), containsInAnyOrder(1))
        assertThat(listOf(1), containsInAnyOrder(equalTo(1)))

        assertThat(listOf(1, 1), containsInAnyOrder(1, 1))
        assertThat(listOf(1, 2), containsInAnyOrder(1, 2))
        assertThat(listOf(2, 1), containsInAnyOrder(1, 2))

        assertThat(listOf(1, 2, 2), containsInAnyOrder(2, 2, 1))
        assertThat(listOf(1, 2, 3), containsInAnyOrder(2, 3, 1))
    }

    @Test
    fun mismatchingIterables() {
        assertThat("one element is different", listOf(1, 2, 3), !containsInAnyOrder(4, 2, 1))
        assertThat("disjoint sets", listOf(1, 2), !containsInAnyOrder(3, 4))
        assertThat("expected is subset", listOf(1, 2, 3), !containsInAnyOrder(1, 2))
        assertThat("actual is subset", listOf(1, 2), !containsInAnyOrder(1, 2, 3))
    }

    @Test
    fun differentAmountOfElements() {
        assertThat(listOf(1, 1), !containsInAnyOrder(1))
        assertThat(listOf(1), !containsInAnyOrder(1, 1))
        assertThat(listOf(1, 2, 2), !containsInAnyOrder(1, 1, 2))
    }

    @Test
    fun descriptions() {
        assertThat(containsInAnyOrder(1, 2).description, equalTo("iterable with elements [is equal to 1, is equal to 2] in any order"))
        assertThat(containsInAnyOrder(1, 2).negatedDescription, equalTo("iterable with elements [is equal to 1, is equal to 2] not matching"))
    }

    @Test
    fun mismatchDescription() {
        assertThat(mismatchDescriptionOf(emptyList(), containsInAnyOrder(1, 2)), equalTo("no element matches: [is equal to 1, is equal to 2] in []"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 4), containsInAnyOrder(1, 2, 3)), equalTo("not matched: 4"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 2), containsInAnyOrder(2, 1, 1)), equalTo("not matched: 2"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 3), containsInAnyOrder(1, 3)), equalTo("not matched: 2"))
        assertThat(mismatchDescriptionOf(listOf(1, 2), containsInAnyOrder(1, 2, 3)), equalTo("no element matches: [is equal to 3] in [1, 2]"))
    }
}

class ContainsExactlyTest {
    @Test
    fun emptyIterable() {
        assertThat(emptyList<Int>(), containsExactly(emptyList()))
        assertThat(emptyList<Int>(), !containsExactly(anything))
        assertThat(listOf(anything), !containsExactly(emptyList<Any>()))
    }

    @Test
    fun matchingIterables() {
        assertThat(listOf(1), containsExactly(1))
        assertThat(listOf(1), containsExactly(equalTo(1)))

        assertThat(listOf(1, 1), containsExactly(1, 1))
        assertThat(listOf(1, 2, 3, 4), containsExactly(1, 2, 3, 4))
    }

    @Test
    fun mismatchingIterables() {
        assertThat("one element is different", listOf(1, 2, 3), !containsExactly(1, 2, 4))
        assertThat("disjoint sets", listOf(1, 2), !containsExactly(3, 4))
        assertThat("expected is subset", listOf(1, 2, 3), !containsExactly(1, 2))
        assertThat("actual is subset", listOf(1, 2), !containsExactly(1, 2, 3))
    }

    @Test
    fun differentAmountOfElements() {
        assertThat(listOf(1, 1), !containsExactly(1))
        assertThat(listOf(1), !containsExactly(1, 1))
    }

    @Test
    fun descriptions() {
        assertThat(containsExactly(1, 2).description, equalTo("iterable with the same elements [is equal to 1, is equal to 2] and exactly the same order"))
        assertThat(containsExactly(1, 2).negatedDescription, equalTo("iterable not the same elements [is equal to 1, is equal to 2] or the exact the same order"))
    }

    @Test
    fun mismatchDescription() {
        assertThat(mismatchDescriptionOf(emptyList(), containsExactly(1, 2)), equalTo("Size of the iterable doesn't match"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 4), containsExactly(1, 2, 3)), equalTo("Following condition doesn't met on index 2: 4 is equal to 3"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 2), containsExactly(2, 1, 1)), equalTo("Following condition doesn't met on index 0: 1 is equal to 2"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 3), containsExactly(1, 3)), equalTo("Size of the iterable doesn't match"))
        assertThat(mismatchDescriptionOf(listOf(1, 2), containsExactly(1, 2, 3)), equalTo("Size of the iterable doesn't match"))
    }
}

class ContainsDuplicatesTest {
    @Test
    fun emptyIterable() {
        assertThat(emptyList(), !containsDuplicates<Int>())
    }

    @Test
    fun matchingIterables() {
        assertThat("one element is duplicated", listOf(1, 2, 1), containsDuplicates())
        assertThat("two elements are duplicated", listOf(1, 2, 1, 2, 3), containsDuplicates())
    }

    @Test
    fun mismatchingIterables() {
        assertThat(listOf(1), !containsDuplicates<Int>())
        assertThat(listOf(1, 2, 3), !containsDuplicates<Int>())
    }

    @Test
    fun descriptions() {
        assertThat(containsDuplicates<Int>().description, equalTo("searches for duplicates in an iterable"))
        assertThat(containsDuplicates<Int>().negatedDescription, equalTo("confirms that there are duplicates in an iterable"))
    }

    @Test
    fun mismatchDescriptions() {
        assertThat(mismatchDescriptionOf(listOf(1, 2), containsDuplicates()), equalTo("No duplicates found!"))
        assertThat(mismatchDescriptionOf(listOf(1, 2, 1, 2), !containsDuplicates<Int>()), equalTo("Following duplicates were found: [1, 2]"))
    }
}

private fun <T> mismatchDescriptionOf(arg: T, matcher: Matcher<T>): String {
    val matchResult = matcher.invoke(arg)
    assertTrue(matchResult is MatchResult.Mismatch, "Precondition: Matcher should not match item.")
    return (matchResult as MatchResult.Mismatch).description
}