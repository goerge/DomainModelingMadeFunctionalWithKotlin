package com.codementor.dmmfwk.ordertaking

import arrow.core.Validated
import arrow.core.invalid
import arrow.core.valid
import java.math.BigDecimal

@JvmInline
value class String50 private constructor(val value: String) {
    companion object {
        fun create(value: String): Validated<String, String50> =
            if (value.length <= 50) String50(value).valid()
            else "String50 must not be more than 50 chars".invalid()
    }
}

@JvmInline
value class EmailAddress private constructor(val value: String) {
    companion object {
        fun create(value: String): Validated<String, EmailAddress> =
            EmailAddress(value).valid()
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
        fun create(value: String): Validated<String, OrderId> =
            OrderId(value).valid()
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
        fun create(value: String): Validated<String, WidgetCode> =
            WidgetCode(value).valid()
    }
}

@JvmInline
value class GizmoCode private constructor(val value: String) {
    companion object {
        fun create(value: String): Validated<String, GizmoCode> =
            GizmoCode(value).valid()
    }
}

sealed class ProductCode
data class Widget(val widgetCode: WidgetCode) : ProductCode()
data class Gizmo(val gizmo: GizmoCode) : ProductCode()

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

sealed class OrderQuantity
data class UnitOrderQuantity(private val unitQuantity: UnitQuantity) : OrderQuantity()
data class KilogramOrderQuantity(private val kilogramQuantity: KilogramQuantity) : OrderQuantity()

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
