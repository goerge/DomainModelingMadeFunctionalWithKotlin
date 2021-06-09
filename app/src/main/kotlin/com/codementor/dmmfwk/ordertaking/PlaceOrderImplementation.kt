package com.codementor.dmmfwk.ordertaking

import arrow.core.Either
import arrow.core.Option
import arrow.core.computations.either

typealias CheckProductCodeExists =
    (ProductCode) -> Boolean

enum class AddressValidationError {
    InvalidFormat,
    AddressNotFound
}

data class CheckedAddress(val address: UnvalidatedAddress)

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

        val lastName = unvalidatedCustomerInfo.firstName
            .let(String50::create)
            .mapLeft(::ValidationError)
            .bind()

        val emailAddress = unvalidatedCustomerInfo.firstName
            .let(EmailAddress::create)
            .mapLeft(::ValidationError)
            .bind()

        CustomerInfo(
            name = PersonalName(firstName = firstName, lastName = lastName),
            emailAddress = emailAddress
        )
    }
