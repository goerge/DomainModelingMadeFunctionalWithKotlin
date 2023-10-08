package com.codementor.dmmfwk.ordertaking

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isRight
import strikt.arrow.value
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.math.BigDecimal

class ToValidatedOrderLineSpec {

    @Test
    fun `creates validated order line if input is valid and product exists`() {
        val productExists: CheckProductCodeExists = { _ -> true }
        val unvalidatedOrderLine = UnvalidatedOrderLine(
            orderLineId = "OL-123",
            productCode = "G123",
            quantity = BigDecimal.valueOf(10.5)
        )

        val result = toValidatedOrderLine(productExists, unvalidatedOrderLine)

        expectThat(result).isRight().value.and {
            get { orderLineId.value } isEqualTo "OL-123"

            get { productCode }
                .isA<ProductCode.Gizmo>()
                .get { gizmoCode.value } isEqualTo "G123"

            get { quantity }
                .isA<OrderQuantity.Kilogram>()
                .get { kilogramQuantity.value } isEqualTo BigDecimal.valueOf(10.5)
        }
    }
}
