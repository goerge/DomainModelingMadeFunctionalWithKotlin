package com.codementor.dmmfwk.ordertaking

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.e
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.arrow.value
import strikt.assertions.isEqualTo

internal class CommonSimpleTypesSpec {

    @Test
    fun `successfully creates a String50 when length requirements are met`() {
        val string = String50.create("Test 123")
        expectThat(string).isValid().value.and {
            get { value } isEqualTo "Test 123"
        }
    }

    @Test
    fun `fails creating a String50 when length requirements are exceeded`() {
        val input = "*".repeat(100)
        val string = String50.create(input)
        expectThat(string).isInvalid().value.isEqualTo("String50 must not be more than 50 chars")
    }
}
