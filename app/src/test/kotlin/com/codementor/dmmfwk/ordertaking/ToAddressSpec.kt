package com.codementor.dmmfwk.ordertaking

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.arrow.isSome
import strikt.arrow.value
import strikt.assertions.isEqualTo

internal class ToAddressSpec {

    @Test
    fun `converts minimal valid CheckedAddress to a ValidatedAddress`() {
        val checkedAddress = CheckedAddress(
            addressLine1 = "Main Street 1/2/3",
            city = "Big City",
            zipCode = "456"
        )

        val result = toAddress(checkedAddress)

        expectThat(result).isRight().value.and {
            get { addressLine1.value } isEqualTo "Main Street 1/2/3"
            get { city.value } isEqualTo "Big City"
            get { zipCode.value } isEqualTo "456"
        }
    }

    @Test
    fun `converts extended valid CheckedAddress to a ValidatedAddress`() {
        val checkedAddress = CheckedAddress(
            addressLine1 = "To",
            addressLine2 = "Mr Banks",
            addressLine3 = "Side Street 1",
            addressLine4 = "Apartment Complex 4",
            city = "Big City",
            zipCode = "456"
        )

        val result = toAddress(checkedAddress)

        expectThat(result).isRight().value.and {
            get { addressLine1.value } isEqualTo "To"
            get { addressLine2 }.isSome().value.and { get {value} isEqualTo "Mr Banks" }
            get { addressLine3 }.isSome().value.and { get {value} isEqualTo "Side Street 1" }
            get { addressLine4 }.isSome().value.and { get {value} isEqualTo "Apartment Complex 4" }
            get { city.value } isEqualTo "Big City"
            get { zipCode.value } isEqualTo "456"
        }
    }

    @Test
    fun `fails converting a CheckedAddress when address line 1 is too long`() {
        val checkedAddress = CheckedAddress(
            addressLine1 = "My Street".repeat(10),
            city = "The City",
            zipCode = "XYZ"
        )

        val result = toAddress(checkedAddress)

        expectThat(result).isLeft()
    }

    @Test
    fun `fails converting a CheckedAddress when city is too long`() {
        val checkedAddress = CheckedAddress(
            addressLine1 = "My Street",
            city = "The City".repeat(10),
            zipCode = "XYZ"
        )

        val result = toAddress(checkedAddress)

        expectThat(result).isLeft()
    }
}
