package ru.yandex.demo.common

enum class Status {
    NEW,
    PAYMENT_RECEIVED,
    RESTAURANT_PROCESSING,
    COURIER_DELIVERY,
    DELIVERED,
    CANCELLED
}

enum class Actor {
    CUSTOMER,
    SUPPORT,
    COURIER,
    RESTAURANT
}

enum class MessageType {
    YOU_MEAL_IS_PREPARING,
    COURIER_IN_DELIVERY,
    CANCELLED
}
