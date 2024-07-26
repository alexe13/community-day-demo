package ru.yandex.demo.xml

import ru.yandex.demo.common.Action
import ru.yandex.demo.common.Guard
import ru.yandex.demo.common.Status

data class GraphEdge(
    var from: Status,
    var to: Status,
    var guards: MutableList<Guard> = mutableListOf(),
    var actions: MutableList<Action> = mutableListOf()
)

