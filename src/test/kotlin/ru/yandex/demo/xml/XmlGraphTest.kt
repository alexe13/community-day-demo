package ru.yandex.demo.xml

import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.yandex.demo.common.Actor
import ru.yandex.demo.common.Order
import ru.yandex.demo.common.Status.DELIVERED
import ru.yandex.demo.common.Status.PAYMENT_RECEIVED
import ru.yandex.demo.common.Status.RESTAURANT_PROCESSING
import ru.yandex.demo.common.StatusChangeEvent
import ru.yandex.demo.common.TransitionNotAllowedException

@SpringBootTest
class XmlGraphTest {

    @Autowired
    lateinit var graph: XmlGraph

    @Test
    fun `verify transitions in xml graph`() {
        assertThatCode {
            graph.changeStatus(
                StatusChangeEvent(
                    actor = Actor.RESTAURANT,
                    order = Order(1, PAYMENT_RECEIVED),
                    newStatus = RESTAURANT_PROCESSING
                )
            )
        }.doesNotThrowAnyException()

        assertThatCode {
            graph.changeStatus(
                StatusChangeEvent(
                    actor = Actor.CUSTOMER,
                    order = Order(1, PAYMENT_RECEIVED),
                    newStatus = RESTAURANT_PROCESSING
                )
            )
        }.isInstanceOf(TransitionNotAllowedException::class.java)

        assertThatCode {
            graph.changeStatus(
                StatusChangeEvent(
                    actor = Actor.RESTAURANT,
                    order = Order(1, PAYMENT_RECEIVED),
                    newStatus = DELIVERED
                )
            )
        }.isInstanceOf(TransitionNotAllowedException::class.java)
    }
}
