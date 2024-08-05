package ru.yandex.demo.dsl

import com.google.common.graph.ImmutableNetwork
import com.google.common.graph.NetworkBuilder
import ru.yandex.demo.common.Status
import ru.yandex.demo.common.StatusChangeEvent
import ru.yandex.demo.common.Action
import ru.yandex.demo.common.Guard

@DslMarker
annotation class MyDsl

fun transitions(init: GraphContext.() -> Unit): DslGraph {
    val context = GraphContext()
    context.init()
    return context.build()
}

@MyDsl
@Suppress("UnstableApiUsage")
class GraphContext {

    private val graph = NetworkBuilder
        .directed()
        .allowsSelfLoops(false)
        .allowsParallelEdges(false)
        .build<Status, GraphEdgeContext>()

    fun transition(statusLink: Pair<Status, Status>, init: GraphEdgeContext.() -> Unit = {}) {
        val edge = GraphEdgeContext(statusLink.first, statusLink.second)
        edge.init()
        graph.addEdge(edge.from, edge.to, edge)
    }

    fun transitions(statusLinks: Pair<List<Status>, Status>, init: GraphEdgeContext.() -> Unit = {}) {
        statusLinks.first.map {
            transition(Pair(it, statusLinks.second), init)
        }
    }

    internal fun build(): DslGraph {
        return DslGraph(ImmutableNetwork.copyOf(graph))
    }
}

@MyDsl
data class GraphEdgeContext(
    val from: Status,
    val to: Status,
    val guards: MutableList<Guard> = mutableListOf(),
    val actions: MutableList<Action> = mutableListOf()
) {
    fun require(init: GuardsContext.() -> Unit) {
        val context = GuardsContext(guards)
        context.init()
    }

    fun actions(init: ActionsContext.() -> Unit) {
        val context = ActionsContext(actions)
        context.init()
    }
}

@MyDsl
class GuardsContext(private val guards: MutableList<Guard>) {
    operator fun Guard.unaryMinus() {
        guards += this
    }

    operator fun ((StatusChangeEvent) -> Boolean).unaryMinus() {
        guards += Guard(this)
    }
}

@MyDsl
class ActionsContext(private val actions: MutableList<Action>) {
    operator fun Action.unaryMinus() {
        actions += this
    }

    operator fun ((StatusChangeEvent) -> Unit).unaryMinus() {
        actions += Action(this)
    }
}


