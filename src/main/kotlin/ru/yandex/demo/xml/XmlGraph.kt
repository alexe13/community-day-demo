package ru.yandex.demo.xml

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.yandex.demo.common.Status
import ru.yandex.demo.common.StatusChangeEvent
import ru.yandex.demo.common.TransitionNotAllowedException
import java.util.EnumMap

@Service
class XmlGraph {

    @Autowired
    private lateinit var edges: List<GraphEdge>
    private var edgeMap: MutableMap<Status, MutableSet<GraphEdge>> = EnumMap(Status::class.java)

    @PostConstruct
    fun init() {
        for (edge in edges) {
            edgeMap.compute(edge.from) { _, v -> v?.apply { add(edge) } ?: mutableSetOf(edge) }
        }
        // println(edges)
        // println(edgeMap)
    }

    fun changeStatus(event: StatusChangeEvent) {
        val edges = edgeMap[event.order.status]
            ?: throw TransitionNotAllowedException("No transitions from status ${event.order.status}")
        val edge = edges.find { it.to == event.newStatus }
            ?: throw TransitionNotAllowedException("No transition found ${event.order.status} -> ${event.newStatus}")

        for (guard in edge.guards) {
            if (!guard.test(event)) {
                throw TransitionNotAllowedException("Transition not allowed")
            }
        }

        edge.actions.forEach { it.fire(event) }
    }
}
