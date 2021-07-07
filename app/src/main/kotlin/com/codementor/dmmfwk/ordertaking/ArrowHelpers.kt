package com.codementor.dmmfwk.ordertaking

import arrow.core.Validated
import arrow.core.valueOr

fun <E, A> Validated<E, A>.getOrThrow() = valueOr { error ->
    throw IllegalStateException("Expected Validated value, but got error: $error")
}
