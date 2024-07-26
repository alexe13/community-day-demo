package ru.yandex.demo.dsl

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.yandex.demo.common.MessageType
import ru.yandex.demo.common.Status.CANCELLED
import ru.yandex.demo.common.Status.COURIER_DELIVERY
import ru.yandex.demo.common.Status.DELIVERED
import ru.yandex.demo.common.Status.NEW
import ru.yandex.demo.common.Status.PAYMENT_RECEIVED
import ru.yandex.demo.common.Status.RESTAURANT_PROCESSING
import ru.yandex.demo.common.Status.entries
import ru.yandex.demo.common.StatusChangeEvent
import ru.yandex.demo.common.RequireCourierActorGuard
import ru.yandex.demo.common.SendPushToUserAction

@Configuration
class DslConfiguration {

    @Bean
    fun dslGraph() = transitions {
        transition(NEW to PAYMENT_RECEIVED)
        transition(PAYMENT_RECEIVED to RESTAURANT_PROCESSING) {
            guards {
                - RequireCourierActorGuard
                - { event: StatusChangeEvent -> event.order.courier != null }
            }
            actions {
                - SendPushToUserAction(MessageType.YOU_MEAL_IS_PREPARING)
            }
        }
        transition(RESTAURANT_PROCESSING to COURIER_DELIVERY)
        transition(COURIER_DELIVERY to DELIVERED)
        transitions(entries.minus(CANCELLED) to CANCELLED)
    }
}
