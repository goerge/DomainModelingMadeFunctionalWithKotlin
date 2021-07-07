package com.codementor.dmmfwk.ordertaking

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isValid
import strikt.arrow.value
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
internal class ToOrderIdSpec {

    @Test
    fun `maps input to OrderId when valid`() {
        val result = toOrderId("123")
        expectThat(result).isValid().value.get { value } isEqualTo "123"
    }
}
