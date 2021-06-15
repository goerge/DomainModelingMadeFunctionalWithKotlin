package com.codementor.dmmfwk.ordertaking

import arrow.core.valid
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isInvalid
import strikt.arrow.isValid
import strikt.arrow.value
import strikt.assertions.isA
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

    @Test
    fun `creates widget code when product code starts with W and is followed by exactly 4 digits`() {
        val string = "W1234"
        val productCode = ProductCode.create(string)
        expectThat(productCode).isValid().value.isA<ProductCode.Widget>().and {
            get { widgetCode.value } isEqualTo "W1234"
        }
    }

    @Test
    fun `creates gizmo code when product code starts with G and is followed by exactly 3 digits`() {
        val string = "G321"
        val productCode = ProductCode.create(string)
        expectThat(productCode).isValid().value.isA<ProductCode.Gizmo>().and {
            get { gizmoCode.value } isEqualTo "G321"
        }
    }

    @Test
    fun `fails creating a product code when it is neither a Widget or Gizmo code`() {
        val string = "X456"
        val productCode = ProductCode.create(string)

        expectThat(productCode).isInvalid().value isEqualTo
            "Product code must start with either W for Widget code or G for Gizmo code"
    }

    @Test
    fun `fails creating a widget code when it has more than 4 digits`() {
        val string = "W12345"
        val productCode = ProductCode.create(string)

        expectThat(productCode).isInvalid().value isEqualTo
            "Widget code must follow format [Wxxxx], where each x is a digit (0-9)"
    }

    @Test
    fun `fails creating a widget code when it has more than 3 digits`() {
        val string = "G1234"
        val productCode = ProductCode.create(string)

        expectThat(productCode).isInvalid().value isEqualTo
            "Gizmo code must follow format [Gxxx], where each x is a digit (0-9)"
    }
}
