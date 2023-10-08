package com.codementor.dmmfwk.ordertaking

import arrow.core.*
import java.math.BigDecimal

@JvmInline
value class String50 private constructor(val value: String) {
    companion object {
        fun create(value: String?): Either<String, String50> =
            when {
                value == null -> String50("").right()
                value.length <= 50 -> String50(value).right()
                else -> "String50 must not be more than 50 chars".left()
            }

        fun createOption(value: String?): Either<String, Option<String50>> =
            if (value == null) none<String50>().right()
            else create(value).map(String50::some)
    }
}

@JvmInline
value class EmailAddress private constructor(val value: String) {
    companion object {
        fun create(value: String?): Either<String, EmailAddress> =
            EmailAddress(value ?: "").right()
    }
}

@JvmInline
value class ZipCode private constructor(val value: String) {
    companion object {
        fun create(value: String): Either<String, ZipCode> =
            ZipCode(value).right()
    }
}

@JvmInline
value class OrderId private constructor(val value: String) {
    companion object {
        fun create(value: String?): Either<String, OrderId> =
            if (value == null) "OrderId is invalid".left()
            else OrderId(value).right()
    }
}

@JvmInline
value class OrderLineId private constructor(val value: String) {
    companion object {
        fun create(value: String): Either<String, OrderLineId> =
            OrderLineId(value).right()
    }
}

@JvmInline
value class WidgetCode private constructor(val value: String) {
    companion object {
        private val widgetCodePattern = Regex("W[0-9]{4}")

        fun create(value: String): Either<String, WidgetCode> =
            if (value.matches(widgetCodePattern)) WidgetCode(value).right()
            else "Widget code must follow format [Wxxxx], where each x is a digit (0-9)".left()
    }
}

@JvmInline
value class GizmoCode private constructor(val value: String) {
    companion object {
        private val gizmoCodePattern = Regex("G[0-9]{3}")

        fun create(value: String): Either<String, GizmoCode> =
            if (value.matches(gizmoCodePattern)) GizmoCode(value).right()
            else "Gizmo code must follow format [Gxxx], where each x is a digit (0-9)".left()
    }
}

sealed class ProductCode {

    companion object {
        fun create(value: String): Either<String, ProductCode> =
            when {
                value.startsWith("W") -> value
                    .let(WidgetCode::create)
                    .map(::Widget)

                value.startsWith("G") -> value
                    .let(GizmoCode::create)
                    .map(::Gizmo)

                else -> "Product code must start with either W for Widget code or G for Gizmo code".left()
            }
    }

    data class Widget(val widgetCode: WidgetCode) : ProductCode() {
        override fun toString(): String = widgetCode.value
    }
    data class Gizmo(val gizmoCode: GizmoCode) : ProductCode() {
        override fun toString(): String = gizmoCode.value
    }
}

@JvmInline
value class UnitQuantity private constructor(val value: Int) {
    companion object {
        fun create(value: Int): Either<String, UnitQuantity> =
            UnitQuantity(value).right()
    }
}

@JvmInline
value class KilogramQuantity private constructor(val value: BigDecimal) {
    companion object {
        fun create(value: BigDecimal): Either<String, KilogramQuantity> =
            KilogramQuantity(value).right()
    }
}

sealed class OrderQuantity {

    companion object {
        fun create(productCode: ProductCode, quantity: BigDecimal): Either<String, OrderQuantity> =
            when (productCode) {
                is ProductCode.Widget -> UnitQuantity.create(quantity.toInt()).map { OrderQuantity.Unit(it) }
                is ProductCode.Gizmo -> KilogramQuantity.create(quantity).map { OrderQuantity.Kilogram(it) }
            }
    }

    data class Unit(val unitQuantity: UnitQuantity) : OrderQuantity()
    data class Kilogram(val kilogramQuantity: KilogramQuantity) : OrderQuantity()
}

@JvmInline
value class Price private constructor(val value: BigDecimal) {
    companion object {
        fun create(value: BigDecimal): Either<String, Price> =
            Price(value).right()
    }
}

@JvmInline
value class BillingAmount private constructor(val value: BigDecimal) {
    companion object {
        fun create(value: BigDecimal): Either<String, BillingAmount> =
            BillingAmount(value).right()
    }
}
