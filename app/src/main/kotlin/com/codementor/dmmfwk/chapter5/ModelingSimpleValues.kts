package com.codementor.dmmfwk.chapter5

import java.math.BigDecimal

@JvmInline
value class CustomerId(val customerId: String)

@JvmInline
value class WidgetCode(val widgetCode: String)

@JvmInline
value class UnitQuantity(val unitQuantity: Int)

@JvmInline
value class KilogramQuantity(val kilogramQuantity: BigDecimal)
