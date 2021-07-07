package com.codementor.dmmfwk.ordertaking

import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.arrow.value
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
internal class ToCheckedAddressSpec {

    @Test
    fun `converts Address to CheckedAddress when address exists`() = runBlockingTest {
        val addressExists: CheckAddressExists = { _ ->
            CheckedAddress(
                addressLine1 = "Main Street 1",
                addressLine2 = null,
                addressLine3 = null,
                addressLine4 = null,
                city = "Great City",
                zipCode = "505050"
            ).right()
        }
        val address = UnvalidatedAddress(
            addressLine1 = "Main Street 1",
            addressLine2 = null,
            addressLine3 = null,
            addressLine4 = null,
            city = "Great City",
            zipCode = "505050"
        )

        val result = toCheckedAddress(addressExists, address)

        expectThat(result).isRight().value.and {
            get { addressLine1 } isEqualTo "Main Street 1"
        }
    }

    @Test
    fun `fails with a ValidationError when address check fails with InvalidFormat`() {
        val addressHasInvalidFormat: CheckAddressExists = { _ -> AddressValidationError.InvalidFormat.left() }

        val address = UnvalidatedAddress(
            addressLine1 = "Very long Street".repeat(10),
            addressLine2 = null,
            addressLine3 = null,
            addressLine4 = null,
            city = "Unknown City",
            zipCode = "60050"
        )

        val result = toCheckedAddress(addressHasInvalidFormat, address)

        expectThat(result).isLeft().value.get { value } isEqualTo "Address has bad format"
    }
}
