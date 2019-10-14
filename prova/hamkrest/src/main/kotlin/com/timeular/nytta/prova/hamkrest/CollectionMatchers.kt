package com.timeular.nytta.prova.hamkrest

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo

fun <T> containsExactly(vararg elements: T, matcher: (T) -> Matcher<T> = { equalTo(it) }): Matcher<Iterable<T>> =
        containsExactly(elements.map { matcher(it) })

fun <T> containsExactly(elements: Iterable<T>, matcher: (T) -> Matcher<T> = { equalTo(it) }): Matcher<Iterable<T>> =
        containsExactly(elements.map { matcher(it) })

fun <T> containsExactly(vararg elementMatchers: Matcher<T>): Matcher<Iterable<T>> =
        containsExactly(elementMatchers.toList())

fun <T> containsExactly(elementMatchers: Iterable<Matcher<T>>): Matcher<Iterable<T>> =
        ContainsExactlyOrder(elementMatchers)

internal class ContainsExactlyOrder<in T>(private val matchers: Iterable<Matcher<T>>) : Matcher<Iterable<T>> {
    override fun invoke(actual: Iterable<T>): MatchResult {
        if (matchers.count() != actual.count()) {
            return MatchResult.Mismatch("Size of the iterable doesn't match")
        }
        val actualList = actual.toList()

        for ((i, matcher) in matchers.withIndex()) {
            val act = actualList[i]
            val res = matcher.invoke(act)
            if (res != MatchResult.Match) {
                return MatchResult.Mismatch("Following condition doesn't met on index $i: $act ${matcher.description}")
            }
        }

        return MatchResult.Match
    }

    override val description: String
        get() = "iterable with the same elements [${matchers.joinToString { it.description }}] and exactly the same order"

    override val negatedDescription: String
        get() = "iterable not the same elements [${matchers.joinToString { it.description }}] or the exact the same order"
}

fun <T> containsInAnyOrder(vararg elements: T, matcher: (T) -> Matcher<T> = { equalTo(it) }): Matcher<Iterable<T>> =
        containsInAnyOrder(elements.map { matcher(it) })

fun <T> containsInAnyOrder(elements: Iterable<T>, matcher: (T) -> Matcher<T> = { equalTo(it) }): Matcher<Iterable<T>> =
        containsInAnyOrder(elements.map { matcher(it) })

fun <T> containsInAnyOrder(vararg elementMatchers: Matcher<T>): Matcher<Iterable<T>> =
        containsInAnyOrder(elementMatchers.toList())

fun <T> containsInAnyOrder(elementMatchers: Iterable<Matcher<T>>): Matcher<Iterable<T>> =
        ContainsInAnyOrder(elementMatchers)

internal class ContainsInAnyOrder<in T>(private val matchers: Iterable<Matcher<T>>) : Matcher<Iterable<T>> {
    override fun invoke(actual: Iterable<T>): MatchResult {
        val matching = Matching(matchers)
        for (element in actual) {
            val matchResult = matching.matches(element)
            if (matchResult != MatchResult.Match) {
                return matchResult
            }
        }
        return matching.isFinished(actual)
    }

    override val description: String
        get() = "iterable with elements [${matchers.joinToString { it.description }}] in any order"

    override val negatedDescription: String
        get() = "iterable with elements [${matchers.joinToString { it.description }}] not matching"

    private class Matching<in S>(matchers: Iterable<Matcher<S>>) {
        private val matchers = matchers.toMutableList()

        fun matches(element: S): MatchResult {
            if (matchers.isEmpty()) {
                return MatchResult.Mismatch("no match for: $element")
            }

            for (matcher in matchers) {
                if (matcher.invoke(element) == MatchResult.Match) {
                    matchers.remove(matcher)
                    return MatchResult.Match
                }
            }
            return MatchResult.Mismatch("not matched: $element")
        }

        fun isFinished(elements: Iterable<S>): MatchResult {
            if (matchers.isEmpty()) {
                return MatchResult.Match
            }
            return MatchResult.Mismatch("no element matches: [${matchers.joinToString { it.description }}] in [${elements.joinToString()}]")
        }
    }
}


fun <T> containsDuplicates() : Matcher<Iterable<T>> = ContainsDuplicates()

internal open class ContainsDuplicates<in T> : Matcher<Iterable<T>> {

    override fun not(): Matcher<Iterable<T>> {
        return object : ContainsDuplicates<T>() {
            override fun invoke(actual: Iterable<T>): MatchResult {
                val actualList = actual.toList()
                if (actualList.size != actual.toSet().size) {
                    val duplicates = HashSet<T>()
                    for ((i, elem) in actualList.withIndex()) {
                        for (j in i..actualList.lastIndex) {
                            if (elem == actualList[j]) {
                                duplicates.add(elem)
                            }
                        }
                    }
                    return MatchResult.Mismatch("Following duplicates were found: $duplicates")
                }

                return MatchResult.Match
            }
        }
    }

    override fun invoke(actual: Iterable<T>): MatchResult =
            if(actual.toList().size == actual.toSet().size) {
                MatchResult.Mismatch("No duplicates found!")
            }else {
                MatchResult.Match
            }

    override val description: String
        get() = "searches for duplicates in an iterable"

    override val negatedDescription: String
        get() = "confirms that there are duplicates in an iterable"
}