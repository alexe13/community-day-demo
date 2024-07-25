package ru.yandex.demo.common

data class StatusChangeEvent(
    val actor: Actor,
    val order: Order,
    val newStatus: Status
)

data class Order(
    val id: Long,
    val status: Status,
    val courier: Courier?
)

data class Courier(
    val name: String,
    val phone: String
)
