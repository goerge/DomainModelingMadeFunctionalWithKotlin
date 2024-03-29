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
sealed interface FruitSnack
@JvmInline
value class Apple(val apple: AppleVariety) : FruitSnack
@JvmInline
value class Banana(val apple: BananaVariety) : FruitSnack
@JvmInline
value class Cherries(val apple: CherryVariety) : FruitSnack

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
@JvmInline
value class ProductCode(val productCode: String)

// Working with Kotlin Types
data class Person(val first: String, val last: String)

val aPerson = Person(first = "Alex", last = "Adams")
val (first, last) = aPerson
// equivalent to
// val first = aPerson.first
// val last = aPerson.last

sealed interface OrderQuantity
@JvmInline
value class UnitQuantity(val quantity: Int) : OrderQuantity
@JvmInline
value class KilogramQuantity(val quantity: BigDecimal) : OrderQuantity

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
@JvmInline
value class CheckNumber(val checkNumber: Int)
@JvmInline
value class CardNumber(val cardNumber: String)
enum class CardType {
    Visa,
    Mastercard
}

data class CreditCardInfo(val cardType: CardType, val cardNumber: CardNumber)

sealed interface PaymentMethod
object Cash : PaymentMethod
@JvmInline
value class Check(val checkNumber: CheckNumber) : PaymentMethod
@JvmInline
value class Card(val creditCardInfo: CreditCardInfo) : PaymentMethod

@JvmInline
value class PaymentAmount(val paymentAmount: BigDecimal)
enum class Currency {
    EUR,
    USD
}

data class Payment(
    val amount: PaymentAmount,
    val currency: Currency,
    val method: PaymentMethod
)

typealias UnpaidInvoice = Nothing
typealias PaidInvoice = Nothing
typealias PayInvoice = (unpaidInvoice: UnpaidInvoice) -> PaidInvoice
typealias ConvertPaymentCurrency = (payment: Payment, currency: Currency) -> Payment

// MODELING OPTIONAL VALUES

// monad
sealed interface Option<out T>
data class Some<T>(val value: T) : Option<T>
object None : Option<Nothing> {
    override fun toString(): String = "None"
}

// nullable value
var nullableString: String? = null

data class PersonalName(
    val firstName: String,
    val middleInitial: Option<String>,
    val lastName: String
)

data class PersonalNameNullable(
    val firstName: String,
    val middleInitial: String?,
    val lastName: String
)

// MODELING ERRORS
sealed class Result<SUCCESS, FAILURE>
data class Ok<T>(val success: T) : Result<T, Nothing>()
data class Error<T>(val failure: T) : Result<Nothing, T>()

typealias PayInvoice = (unpaidInvoice: UnpaidInvoice, payment: Payment) -> Result<PaidInvoice, PaymentError>

enum class PaymentError {
    CardTypeNotRecognized,
    PaymentRejected,
    PaymentProviderOffline
}

// MODELING NO VALUE AT ALL
typealias Customer = Nothing
typealias SaveCustomer = (customer: Customer) -> Unit

typealias NextRandom = () -> Int

// MODELING LISTS AND COLLECTIONS
typealias OrderId = Nothing
typealias OrderLine = Nothing
data class Order(
    val orderId: OrderId,
    val lines: List<OrderLine>
)

val aList = listOf(1, 2, 3)

val aNewList = listOf(0) + aList

fun <T> T.cons(tail: List<T>): List<T> = listOf(this) + tail

val anotherList = 0.cons(aList) // new list is [0, 1, 2, 3]

// Kotlin does not support list deconstruction in pattern matching
fun <T> printList(list: List<T>) {
    return when (list.size) {
        0 -> println("list is empty")
        1 -> println("list has on element ${list[0]}")
        2 -> println("list has two elements: ${list[0]} and ${list[1]}")
        else -> println("list has more than two elements")
    }
}
