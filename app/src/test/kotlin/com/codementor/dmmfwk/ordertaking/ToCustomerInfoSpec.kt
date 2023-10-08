package com.codementor.dmmfwk.ordertaking

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.arrow.value
import strikt.assertions.isEqualTo

internal class ToCustomerInfoSpec {

    @Test
    fun `converts a valid UnvalidatedCustomerInfo to ValidatedCustomerInfo`() {
        val unvalidatedCustomerInfo = UnvalidatedCustomerInfo(
            firstName = "Peter",
            lastName = "Parker",
            emailAddress = "peter.parker@example.com"
        )

        val result = toCustomerInfo(unvalidatedCustomerInfo)

        expectThat(result).isRight().value.and {
            get { name.firstName.value } isEqualTo "Peter"
            get { name.lastName.value } isEqualTo "Parker"
            get { emailAddress.value } isEqualTo "peter.parker@example.com"
        }
    }

    @Test
    fun `fails to validate CustomerInfo when firstName is too long`() {
        val unvalidatedCustomerInfo = UnvalidatedCustomerInfo(
            firstName = "John".repeat(20),
            lastName = "Doe",
            emailAddress = "peter.parker@example.com"
        )

        val result = toCustomerInfo(unvalidatedCustomerInfo)

        expectThat(result).isLeft().value.and {
            get { value } isEqualTo "String50 must not be more than 50 chars"
        }
    }
}
