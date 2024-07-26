package ru.yandex.demo.common

data class StatusChangeEvent(
    val actor: Actor,
    val order: Order,
    val newStatus: Status
)

data class Order(
    val id: Long,
    val status: Status,
    val courier: Courier? = null
)

data class Courier(
    val name: String,
    val phone: String
)

fun interface Guard {
    fun test(event: StatusChangeEvent): Boolean
}

data object RequireRestaurantActorGuard : Guard {
    override fun test(event: StatusChangeEvent): Boolean {
        return event.actor == Actor.RESTAURANT
    }
}

data object RequireCourierPresentGuard : Guard {
    override fun test(event: StatusChangeEvent): Boolean {
        return event.order.courier != null
    }
}

data object RequireCourierActorGuard : Guard {
    override fun test(event: StatusChangeEvent): Boolean {
        return event.actor == Actor.COURIER
    }
}

fun interface Action {
    fun fire(event: StatusChangeEvent)
}

class SendPushToUserAction(private val messageType: MessageType) : Action {
    override fun fire(event: StatusChangeEvent) {
        sendPushToUser(messageType)
    }
}

fun sendPushToUser(messageType: MessageType) {
    // stub
    println("Sending $messageType")
}
