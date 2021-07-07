package com.codementor.dmmfwk.ordertaking

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.arrow.value
import strikt.assertions.isA
import strikt.assertions.isEqualTo

internal class ToProductCodeSpec {

    @Test
    fun `creates a ProductCode if it exists and is valid`() {
        val productCodeExists: CheckProductCodeExists = { _ -> true }

        val result = toProductCode(productCodeExists, "G123")

        expectThat(result).isRight().value
            .isA<ProductCode.Gizmo>()
            .get { gizmoCode.value } isEqualTo "G123"
    }

    @Test
    fun `fails creating a ProductCode when it does not exist`() {
        val productCodeDoesNotExist: CheckProductCodeExists = { _ -> false }

        val result = toProductCode(productCodeDoesNotExist, "G456")

        expectThat(result).isLeft().value.get { value } isEqualTo "Invalid: G456"
    }

    @Test
    fun `fails creating a ProductCode when it is invalid`() {
        val productCodeExists: CheckProductCodeExists = { _ -> true }

        val result = toProductCode(productCodeExists, "X12345")

        expectThat(result).isLeft().value.get { value }
            .isEqualTo("Product code must start with either W for Widget code or G for Gizmo code")
    }
}
