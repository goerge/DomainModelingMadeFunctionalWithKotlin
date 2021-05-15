package com.codementor.dmmfwk

import java.math.BigDecimal

// Type Signatures

fun add1(x: Int): Int = x + 1

fun add(x: Int, y: Int): Int = x + y

fun squarePlusOne(x: Int): Int {
    val square = x * x
    return square + 1
}

// Functions with Generic Types

fun <T> areEqual(x: T, y: T): Boolean = x == y

// Composition of Types

// "AND" TYPES
data class FruitSalad(
    val apple: AppleVariety,
    val banana: BananaVariety,
    val cherries: CherryVariety
)

// "OR" TYPES
sealed class FruitSnack
data class Apple(val apple: AppleVariety) : FruitSnack()
data class Banana(val apple: BananaVariety) : FruitSnack()
data class Cherries(val apple: CherryVariety) : FruitSnack()

enum class AppleVariety {
    GoldenDelicious,
    GrannySmith,
    Fuji
}

enum class BananaVariety {
    Cavendish,
    GrosMichel,
    Manzano
}

enum class CherryVariety {
    Montmorency,
    Bing
}

// SIMPLE TYPES
data class ProductCode(val productCode: String)

// Working with Kotlin Types
data class Person(val first: String, val last: String)

val aPerson = Person(first = "Alex", last = "Adams")
val (first, last) = aPerson
// equivalent to
// val first = aPerson.first
// val last = aPerson.last

sealed class OrderQuantity
data class UnitQuantity(val quantity: Int) : OrderQuantity()
data class KilogramQuantity(val quantity: BigDecimal) : OrderQuantity()

val anOrderQtyInUnits = UnitQuantity(10)
val anOrderQtyInKg = KilogramQuantity("2.5".toBigDecimal())

fun printQuantity(aOrderQuantity: OrderQuantity) {
    when (aOrderQuantity) {
        is UnitQuantity -> println("${aOrderQuantity.quantity} units")
        is KilogramQuantity -> println("${aOrderQuantity.quantity} kg")
    }
}

printQuantity(anOrderQtyInUnits)
printQuantity(anOrderQtyInKg)

// Building a Domain Model by Composing Types
data class CheckNumber(val checkNumber: Int)
data class CardNumber(val cardNumber: String)
enum class CardType {
    Visa,
    Mastercard
}
data class CreditCardInfo(val cardType: CardType, val cardNumber: CardNumber)

sealed class PaymentMethod
object Cash : PaymentMethod()
data class Check(val checkNumber: CheckNumber) : PaymentMethod()
data class Card(val creditCardInfo: CreditCardInfo) : PaymentMethod()

data class PaymentAmount(val paymentAmount: BigDecimal)
enum class Currency {
    EUR,
    USD
}

data class Payment(
    val amount: PaymentAmount,
    val currency: Currency,
    val method: PaymentMethod
)

fun payInvoice(unpaidInvoice: UnpaidInvoice): PaidInvoice {}

fun convertPaymentCurrency(payment: Payment, currency: Currency): Payment {}
