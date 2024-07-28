package ru.yandex.demo.dsl

import ru.yandex.demo.common.Status
import ru.yandex.demo.common.Status.CANCELLED
import ru.yandex.demo.common.Status.NEW
import ru.yandex.demo.xml.GraphEdge

object transition

infix fun transition.from(from: Status): FromWrapper = FromWrapper(from)

class FromWrapper(val fromStatus: Status) {
    infix fun to(toStatus: Status): GraphEdge {
        return GraphEdge(fromStatus, toStatus)
    }
}

fun test() {
    val t = transition from NEW to CANCELLED
    println(t)
}
