package com.codementor.dmmfwk.chapter5

// define some types
@JvmInline
value class CustomerId(val customerId: Int)

@JvmInline
value class OrderId(val orderId: Int)

// define some values
val customerId = CustomerId(42)
val orderId = OrderId(42)

// does not compile
// println(customerId == orderId)
// Operator '==' cannot be applied to 'CustomerId' and 'OrderId'

// define a function using a CustomerId
fun processCustomerId(id: CustomerId) = Unit

// call it with an OrderId -- compiler error!
//processCustomerId(orderId)
// Type mismatch: inferred type is OrderId but CustomerId was expected

// construct
data class TheAnswer(val answer: Int)
val answer = TheAnswer(42)

// deconstruct
val (innerValue) = answer
//              ^ innerValue is set to 42

println(innerValue)  // prints "42"
