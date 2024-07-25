package ru.yandex.demo.xml

import ru.yandex.demo.common.Actor
import ru.yandex.demo.common.MessageType
import ru.yandex.demo.common.Status
import ru.yandex.demo.common.StatusChangeEvent

sealed interface Guard {
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

sealed interface Action {
    fun fire(event: StatusChangeEvent)
}

class SendPushToUserAction(private val messageType: MessageType) : Action {
    override fun fire(event: StatusChangeEvent) {
        sendPushToUser(messageType)
    }
}

data class GraphEdge(
    var from: Status,
    var to: Status,
    var guards: List<Guard> = emptyList(),
    var actions: List<Action> = emptyList()
)

fun sendPushToUser(messageType: MessageType) {
    // stub
    println("Sending $messageType")
}
