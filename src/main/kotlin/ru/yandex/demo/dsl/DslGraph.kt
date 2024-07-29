package ru.yandex.demo.dsl

import com.google.common.graph.ImmutableNetwork
import ru.yandex.demo.common.Status
import ru.yandex.demo.common.StatusChangeEvent
import ru.yandex.demo.common.TransitionNotAllowedException

class DslGraph(private val graph: ImmutableNetwork<Status, GraphEdgeContext>) {

    fun changeStatus(event: StatusChangeEvent) {
        val from = event.order.status
        val to = event.newStatus
        if (!graph.hasEdgeConnecting(from, to)) {
            throw TransitionNotAllowedException("No transitions from status $from to status $to")
        }

        val edge = graph.edgeConnecting(from, to).orElseThrow()

        for (guard in edge.guards) {
            if (!guard.check(event)) {
                throw TransitionNotAllowedException("Transition not allowed")
            }
        }

        edge.actions.forEach { it.fire(event) }
    }
}
