package com.codementor.dmmfwk.ordertaking

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isRight
import strikt.arrow.value
import strikt.assertions.isEqualTo

internal class ToOrderIdSpec {

    @Test
    fun `maps input to OrderId when valid`() {
        val result = toOrderId("123")
        expectThat(result).isRight().value.get { value } isEqualTo "123"
    }
}
