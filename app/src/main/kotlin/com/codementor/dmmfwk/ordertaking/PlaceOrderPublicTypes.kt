package com.codementor.dmmfwk.ordertaking

import arrow.core.Either
import java.lang.Exception
import java.math.BigDecimal
import java.net.URI

data class UnvalidatedCustomerInfo(
    val firstName: String? = null,
    val lastName: String? = null,
    val emailAddress: String? = null
)

data class UnvalidatedAddress(
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val addressLine3: String? = null,
    val addressLine4: String? = null,
    val city: String? = null,
    val zipCode: String? = null
)

data class UnvalidatedOrderLine(
    val orderLineId: String,
    val productCode: String,
    val quantity: BigDecimal
)

data class UnvalidatedOrder(
    val orderId: String?,
    val customerInfo: UnvalidatedCustomerInfo = UnvalidatedCustomerInfo(),
    val shippingAddress: UnvalidatedAddress = UnvalidatedAddress(),
    val billingAddress: UnvalidatedAddress = UnvalidatedAddress(),
    val lines: List<UnvalidatedOrderLine> = emptyList()
)

sealed class PlacedOrderEvent

data class OrderAcknowledgementSent(
    val orderId: OrderId,
    val emailAddress: EmailAddress
) : PlacedOrderEvent()

data class PricedOrderLine(
    val orderLineId: OrderLineId,
    val productCode: ProductCode,
    val quantity: OrderQuantity,
    val linePrice: Price
)

data class PricedOrder(
    val orderId: OrderId,
    val customerInfo: CustomerInfo,
    val shippingAddress: Address,
    val billingAddress: Address,
    val amountToBill: BillingAmount,
    val lines: List<PricedOrderLine>
)

data class OrderPlaced(
    val order: PricedOrder
) : PlacedOrderEvent()

data class BillableOrderPlaced(
    val orderId: OrderId,
    val billingAddress: Address,
    val amountToBill: BillingAmount
) : PlacedOrderEvent()

sealed class PlaceOrderError {
    data class Validation(val validationError: ValidationError) : PlaceOrderError()
    data class Pricing(val pricingError: PricingError) : PlaceOrderError()
    data class RemoteService(val remoteServiceError: RemoteServiceError) : PlaceOrderError()
}

data class ValidationError(val value: String)

data class PricingError(val value: String)

data class RemoteServiceError(
    val service: ServiceInfo,
    val exception: Exception
)

data class ServiceInfo(
    val name: String,
    val endpoint: URI
)

typealias PlaceOrder =
    (UnvalidatedOrder) -> Either<List<PlaceOrderError>, PlacedOrderEvent>
