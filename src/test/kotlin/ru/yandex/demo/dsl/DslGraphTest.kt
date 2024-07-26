package ru.yandex.demo.dsl

import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.yandex.demo.common.Actor.RESTAURANT
import ru.yandex.demo.common.Order
import ru.yandex.demo.common.Status
import ru.yandex.demo.common.StatusChangeEvent
import ru.yandex.demo.common.TransitionNotAllowedException

@SpringBootTest
class DslGraphTest {

    @Autowired
    lateinit var graph: DslGraph

    @Test
    fun `verify transitions in dsl graph`() {
        assertThatCode {
            graph.changeStatus(
                StatusChangeEvent(
                    actor = RESTAURANT,
                    order = Order(1, Status.RESTAURANT_PROCESSING),
                    newStatus = Status.CANCELLED
                )
            )
        }.doesNotThrowAnyException()

        assertThatCode {
            graph.changeStatus(
                StatusChangeEvent(
                    actor = RESTAURANT,
                    order = Order(1, Status.RESTAURANT_PROCESSING),
                    newStatus = Status.DELIVERED
                )
            )
        }.isInstanceOf(TransitionNotAllowedException::class.java)
    }
}
