package com.codementor.dmmfwk.ordertaking

import arrow.core.*
import java.math.BigDecimal

@JvmInline
value class String50 private constructor(val value: String) {
    companion object {
        fun create(value: String?): Validated<String, String50> =
            when {
                value == null -> String50("").valid()
                value.length <= 50 -> String50(value).valid()
                else -> "String50 must not be more than 50 chars".invalid()
            }

        fun createOption(value: String?): Validated<String, Option<String50>> =
            if (value == null) none<String50>().valid()
            else create(value).map(String50::some)
    }
}

@JvmInline
value class EmailAddress private constructor(val value: String) {
    companion object {
        fun create(value: String?): Validated<String, EmailAddress> =
            EmailAddress(value ?: "").valid()
    }
}

@JvmInline
value class ZipCode private constructor(val value: String) {
    companion object {
        fun create(value: String): Validated<String, ZipCode> =
            ZipCode(value).valid()
    }
}

@JvmInline
value class OrderId private constructor(val value: String) {
    companion object {
        fun create(value: String?): Validated<String, OrderId> =
            if (value == null) "OrderId is invalid".invalid()
            else OrderId(value).valid()
    }
}

@JvmInline
value class OrderLineId private constructor(val value: String) {
    companion object {
        fun create(value: String): Validated<String, OrderLineId> =
            OrderLineId(value).valid()
    }
}

@JvmInline
value class WidgetCode private constructor(val value: String) {
    companion object {
        private val widgetCodePattern = Regex("W[0-9]{4}")

        fun create(value: String): Validated<String, WidgetCode> =
            if (value.matches(widgetCodePattern)) WidgetCode(value).valid()
            else "Widget code must follow format [Wxxxx], where each x is a digit (0-9)".invalid()
    }
}

@JvmInline
value class GizmoCode private constructor(val value: String) {
    companion object {
        private val gizmoCodePattern = Regex("G[0-9]{3}")

        fun create(value: String): Validated<String, GizmoCode> =
            if (value.matches(gizmoCodePattern)) GizmoCode(value).valid()
            else "Gizmo code must follow format [Gxxx], where each x is a digit (0-9)".invalid()
    }
}

sealed class ProductCode {

    companion object {
        fun create(value: String): Validated<String, ProductCode> =
            when {
                value.startsWith("W") -> value
                    .let(WidgetCode::create)
                    .map(::Widget)

                value.startsWith("G") -> value
                    .let(GizmoCode::create)
                    .map(::Gizmo)

                else -> "Product code must start with either W for Widget code or G for Gizmo code".invalid()
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
        fun create(value: Int): Validated<String, UnitQuantity> =
            UnitQuantity(value).valid()
    }
}

@JvmInline
value class KilogramQuantity private constructor(val value: BigDecimal) {
    companion object {
        fun create(value: BigDecimal): Validated<String, KilogramQuantity> =
            KilogramQuantity(value).valid()
    }
}

sealed class OrderQuantity {

    companion object {
        fun create(productCode: ProductCode, quantity: BigDecimal): Validated<String, OrderQuantity> =
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
        fun create(value: BigDecimal): Validated<String, Price> =
            Price(value).valid()
    }
}

@JvmInline
value class BillingAmount private constructor(val value: BigDecimal) {
    companion object {
        fun create(value: BigDecimal): Validated<String, BillingAmount> =
            BillingAmount(value).valid()
    }
}
