package com.codementor.dmmfwk.ordertaking

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.arrow.value
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
internal class ToCustomerInfoSpec {

    @Test
    fun `converts a valid UnvalidatedCustomerInfo to ValidatedCustomerInfo`() = runBlockingTest {
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
    fun `fails to validate CustomerInfo when firstName is too long`() = runBlockingTest {
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
