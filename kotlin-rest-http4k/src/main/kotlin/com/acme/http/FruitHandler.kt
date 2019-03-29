package com.acme.http

import com.acme.model.Fruit
import com.acme.model.FruitRepository
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.SunHttp
import org.http4k.server.asServer

class FruitHandler(val fruits: FruitRepository) : HttpHandler {

    override fun invoke(request: Request): Response =
        routes(
            "fruits" bind routes(
                "/" bind GET to {

                    Response(OK).body(fruits.getAll().toString())
                },
                "/" bind POST to {
                    fruits.addFruit(it.bodyString())
                    Response(CREATED)
                },
                "/{id}" bind GET to {
                    val fruit = fruits.getById(it.path("id")?.toIntOrNull() ?: 0)

                    Response(OK).body(fruit.toString())
                },
                "/{id}" bind PUT to {
                    val fruit = Fruit(it.path("id")?.toIntOrNull() ?: 0, it.bodyString())
                    fruits.replace(fruit)

                    Response(OK).body(fruit.toString())
                },
                "/{id}" bind DELETE to {
                    fruits.remove(it.path("id")?.toIntOrNull() ?: 0)
                    Response(ACCEPTED)
                }
            ),
            static(Classpath("static"))
        )(request)
}

fun main() {
    FruitHandler(FruitRepository()).asServer(SunHttp(9000)).start()
}