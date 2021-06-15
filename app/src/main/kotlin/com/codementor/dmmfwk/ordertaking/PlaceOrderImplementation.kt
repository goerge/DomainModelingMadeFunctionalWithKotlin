package com.codementor.dmmfwk.ordertaking

import arrow.core.*
import arrow.core.computations.either

typealias CheckProductCodeExists =
    (ProductCode) -> Boolean

enum class AddressValidationError {
    InvalidFormat,
    AddressNotFound
}

data class CheckedAddress(
    val addressLine1: String,
    val addressLine2: String? = null,
    val addressLine3: String? = null,
    val addressLine4: String? = null,
    val city: String,
    val zipCode: String
)

typealias CheckAddressExists =
    (UnvalidatedAddress) -> Either<AddressValidationError, CheckedAddress>

data class ValidatedOrderLine(
    val orderLineId: OrderLineId,
    val productCode: ProductCode,
    val quantity: OrderQuantity
)

data class ValidatedOrder(
    val orderId: OrderId,
    val customerInfo: CustomerInfo,
    val shippingAddress: Address,
    val billingAddress: Address,
    val lines: List<ValidatedOrderLine>
)

typealias ValidateOrder =
    (CheckProductCodeExists, CheckAddressExists, UnvalidatedOrder) -> Either<ValidationError, ValidatedOrder>

typealias GetProductPrice =
    (ProductCode) -> Price

typealias PriceOrder =
    (GetProductPrice, ValidatedOrder) -> Either<PricingError, PricedOrder>

data class HtmlString(val value: String)

data class OrderAcknowledgement(
    val emailAddress: EmailAddress,
    val letter: HtmlString
)

typealias CreateOrderAcknowledgementLetter =
    (PricedOrder) -> HtmlString

enum class SendResult {
    Sent,
    NotSent
}

typealias SendOrderAcknowledgement =
    (OrderAcknowledgement) -> SendResult

typealias AcknowledgeOrder =
    (CreateOrderAcknowledgementLetter, SendOrderAcknowledgement, PricedOrder) -> Option<OrderAcknowledgementSent>

typealias CreateEvents =
    (PricedOrder, Option<OrderAcknowledgementSent>) -> List<PlacedOrderEvent>

suspend fun toCustomerInfo (unvalidatedCustomerInfo: UnvalidatedCustomerInfo): Either<ValidationError, CustomerInfo> =
    either {
        val firstName = unvalidatedCustomerInfo.firstName
            .let(String50::create)
            .mapLeft(::ValidationError)
            .bind()

        val lastName = unvalidatedCustomerInfo.lastName
            .let(String50::create)
            .mapLeft(::ValidationError)
            .bind()

        val emailAddress = unvalidatedCustomerInfo.emailAddress
            .let(EmailAddress::create)
            .mapLeft(::ValidationError)
            .bind()

        CustomerInfo(
            name = PersonalName(firstName = firstName, lastName = lastName),
            emailAddress = emailAddress
        )
    }

suspend fun toAddress(checkedAddress: CheckedAddress): Either<ValidationError, Address> =
    either {
        val addressLine1 = checkedAddress.addressLine1
            .let(String50::create)
            .mapLeft(::ValidationError)
            .bind()

        val addressLine2 = checkedAddress.addressLine2
            .let(String50::createOption)
            .mapLeft(::ValidationError)
            .bind()

        val addressLine3 = checkedAddress.addressLine3
            .let(String50::createOption)
            .mapLeft(::ValidationError)
            .bind()

        val addressLine4 = checkedAddress.addressLine4
            .let(String50::createOption)
            .mapLeft(::ValidationError)
            .bind()

        val city = checkedAddress.city
            .let(String50::create)
            .mapLeft(::ValidationError)
            .bind()

        val zipCode = checkedAddress.zipCode
            .let(ZipCode::create)
            .mapLeft(::ValidationError)
            .bind()

        Address(
            addressLine1 = addressLine1,
            addressLine2 = addressLine2,
            addressLine3 = addressLine3,
            addressLine4 = addressLine4,
            city = city,
            zipCode = zipCode
        )
    }
