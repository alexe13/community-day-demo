package ru.yandex.demo.dsl

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.yandex.demo.common.CourierShouldBeAssigned
import ru.yandex.demo.common.InitiatedByCourier
import ru.yandex.demo.common.InitiatedByRestaurant
import ru.yandex.demo.common.NotifyCourierDelivery
import ru.yandex.demo.common.NotifyMealPreparing
import ru.yandex.demo.common.NotifyOrderCancelled
import ru.yandex.demo.common.ShowCourierTrack
import ru.yandex.demo.common.StartCourierSearch
import ru.yandex.demo.common.Status.CANCELLED
import ru.yandex.demo.common.Status.COURIER_DELIVERY
import ru.yandex.demo.common.Status.DELIVERED
import ru.yandex.demo.common.Status.NEW
import ru.yandex.demo.common.Status.PAYMENT_RECEIVED
import ru.yandex.demo.common.Status.RESTAURANT_PROCESSING
import ru.yandex.demo.common.Status.entries

@Configuration
class DslConfiguration {

    @Bean
    fun dslGraph() = transitions {
        transition(NEW to PAYMENT_RECEIVED)
        transition(PAYMENT_RECEIVED to RESTAURANT_PROCESSING) {
            require {
                - InitiatedByRestaurant
            }
            actions {
                - NotifyMealPreparing
                - StartCourierSearch
            }
        }
        transition(RESTAURANT_PROCESSING to COURIER_DELIVERY) {
            require {
                - CourierShouldBeAssigned
            }
            actions {
                - NotifyCourierDelivery
                - ShowCourierTrack
            }
        }
        transition(COURIER_DELIVERY to DELIVERED) {
            require {
                - InitiatedByCourier
            }
        }
        transitions(entries.minus(CANCELLED) to CANCELLED) {
            actions {
                - NotifyOrderCancelled
            }
        }
    }
}
