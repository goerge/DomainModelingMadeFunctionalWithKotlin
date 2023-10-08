package com.codementor.dmmfwk.ordertaking

import arrow.core.*
import arrow.core.raise.either
import java.math.BigDecimal

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

fun toCustomerInfo(unvalidatedCustomerInfo: UnvalidatedCustomerInfo): Either<ValidationError, CustomerInfo> =
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

fun toAddress(checkedAddress: CheckedAddress): Either<ValidationError, Address> =
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

fun toCheckedAddress(
    checkAddress: CheckAddressExists,
    address: UnvalidatedAddress
): Either<ValidationError, CheckedAddress> =
    checkAddress(address).mapLeft { addressValidationError ->
        when (addressValidationError) {
            AddressValidationError.AddressNotFound -> ValidationError("Address not found")
            AddressValidationError.InvalidFormat -> ValidationError("Address has bad format")
        }
    }

fun toOrderId(orderId: String?): Either<ValidationError, OrderId> =
    OrderId.create(orderId).mapLeft { error -> ValidationError(error) }

fun toOrderLineId(orderLineId: String): Either<ValidationError, OrderLineId> =
    OrderLineId.create(orderLineId).mapLeft { error -> ValidationError(error) }

fun toProductCode(
    checkProductCodeExists: CheckProductCodeExists,
    productCode: String
): Either<ValidationError, ProductCode> {

    fun checkProduct(productCode: ProductCode) =
        if (checkProductCodeExists(productCode)) productCode.right()
        else ValidationError("Invalid: $productCode").left()

    return ProductCode.create(productCode)
        .mapLeft { error -> ValidationError(error) }
        .flatMap(::checkProduct)
}

fun toOrderQuantity(
    productCode: ProductCode,
    quantity: BigDecimal
): Either<ValidationError, OrderQuantity> =
    OrderQuantity.create(productCode, quantity)
        .mapLeft { error -> ValidationError(error) }

fun toValidatedOrderLine(
    checkedProductCodeExists: CheckProductCodeExists,
    unvalidatedOrderLine: UnvalidatedOrderLine
): Either<ValidationError, ValidatedOrderLine> =
    either {
        val orderLineId = toOrderLineId(unvalidatedOrderLine.orderLineId).bind()
        val productCode = toProductCode(checkedProductCodeExists, unvalidatedOrderLine.productCode).bind()
        val quantity = toOrderQuantity(productCode, unvalidatedOrderLine.quantity).bind()

        ValidatedOrderLine(
            orderLineId = orderLineId,
            productCode = productCode,
            quantity = quantity
        )
    }

fun validateOrder(
    checkProductCodeExists: CheckProductCodeExists,
    checkAddress: CheckAddressExists,
    unvalidatedOrder: UnvalidatedOrder
): Either<ValidationError, ValidatedOrder> =
    either {
        val orderId = toOrderId(unvalidatedOrder.orderId).bind()
        val customerInfo = toCustomerInfo(unvalidatedOrder.customerInfo).bind()
        val shippingAddress = toCheckedAddress(checkAddress, unvalidatedOrder.shippingAddress)
            .flatMap { checkedAddress -> toAddress(checkedAddress) }
            .bind()
        val billingAddress = toCheckedAddress(checkAddress, unvalidatedOrder.billingAddress)
            .flatMap { checkedAddress -> toAddress(checkedAddress) }
            .bind()
        val lines = unvalidatedOrder.lines
            .map { line -> toValidatedOrderLine(checkProductCodeExists, line) }
            .bindAll()

        ValidatedOrder(
            orderId = orderId,
            customerInfo = customerInfo,
            shippingAddress = shippingAddress,
            billingAddress = billingAddress,
            lines = lines
        )
    }
