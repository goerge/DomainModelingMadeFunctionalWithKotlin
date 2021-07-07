package com.codementor.dmmfwk.ordertaking

import arrow.core.right
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isRight
import strikt.arrow.value
import strikt.assertions.isEqualTo
import strikt.assertions.single
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
internal class ValidateOrderSpec {

    @Test
    fun `creates ValidatedOrder if input is valid and products exist`() = runBlockingTest {
        val productCodeExists: CheckProductCodeExists = { _ -> true }
        val addressExists: CheckAddressExists = { _ ->
            CheckedAddress(
                addressLine1 = "My Street 1",
                city = "Best City in Town",
                zipCode = "2342"
            ).right()
        }
        val unvalidatedOrder = UnvalidatedOrder(
            orderId = "OR-123",
            customerInfo = UnvalidatedCustomerInfo(
                firstName = "Peter",
                lastName = "Parker",
                emailAddress = "peter.parker@example.com"
            ),
            shippingAddress = UnvalidatedAddress(
                addressLine1 = "My Street 1",
                city = "Best City in Town",
                zipCode = "2342"
            ),
            billingAddress = UnvalidatedAddress(
                addressLine1 = "My Street 1",
                city = "Best City in Town",
                zipCode = "2342"
            ),
            lines = listOf(
                UnvalidatedOrderLine("OL-123", "W1234", BigDecimal.TEN)
            )
        )

        val result = validateOrder(productCodeExists, addressExists, unvalidatedOrder)

        expectThat(result).isRight().value.and {
            get { lines }.single().and {
                get { orderLineId.value } isEqualTo "OL-123"
            }
        }
    }
}
