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
    fun check(event: StatusChangeEvent): Boolean
}

data object InitiatedByRestaurant : Guard {
    override fun check(event: StatusChangeEvent): Boolean {
        return event.actor == Actor.RESTAURANT
    }
}

data object CourierShouldBeAssigned : Guard {
    override fun check(event: StatusChangeEvent): Boolean {
        return event.order.courier != null
    }
}

data object InitiatedByCourier : Guard {
    override fun check(event: StatusChangeEvent): Boolean {
        return event.actor == Actor.COURIER
    }
}

fun interface Action {
    fun fire(event: StatusChangeEvent)
}

data object NotifyMealPreparing: Action {
    override fun fire(event: StatusChangeEvent) {
        sendPushToUser(MessageType.YOU_MEAL_IS_PREPARING)
    }
}

data object NotifyCourierDelivery: Action {
    override fun fire(event: StatusChangeEvent) {
        sendPushToUser(MessageType.COURIER_IN_DELIVERY)
    }
}

data object NotifyOrderCancelled: Action {
    override fun fire(event: StatusChangeEvent) {
        sendPushToUser(MessageType.CANCELLED)
    }
}


data object StartCourierSearch: Action {
    override fun fire(event: StatusChangeEvent) {
        println("Searching courier...")
    }
}

data object ShowCourierTrack: Action {
    override fun fire(event: StatusChangeEvent) {
        // STUB
    }
}


fun sendPushToUser(messageType: MessageType) {
    // stub
    println("Sending $messageType")
}
