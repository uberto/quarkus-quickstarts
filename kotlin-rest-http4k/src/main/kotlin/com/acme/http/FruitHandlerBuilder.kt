package com.acme.http

import com.acme.model.Fruit
import com.acme.model.FruitRepository
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import java.io.File

class FruitHandlerBuilder(val fruits: FruitRepository) {

    private fun id(req: Request) = req.path("id")?.toInt() ?: 0

    fun createHandler() =
        ServerFilters.CatchAll()
            .then(
                routes(
                    "fruits" bind routes(
                        "/" bind GET to {
                            val fruits = fruits.getAll().toJson()
                            Response(OK).body(fruits)
                        },
                        "/" bind POST to {
                            val fruitName = it.bodyString().getName()
                            if (!fruitName.isBlank())
                                fruits.addFruit(fruitName)
                            Response(CREATED)
                        },
                        "/{id}" bind GET to {
                            val fruit = fruits.getById(id(it))
                            Response(OK).body(fruit?.toJson().orEmpty())
                        },
                        "/{id}" bind PUT to {
                            val fruit = Fruit(id(it), it.bodyString().getName())
                            fruits.replace(fruit)

                            Response(OK).body(fruit.toString())
                        },
                        "/{id}" bind DELETE to {
                            fruits.remove(id(it))
                            Response(NO_CONTENT)
                        }
                    ),
                    "/" bind GET to {
                        val asset = File("../../public/static/index.html") //it runs inside target/classes
                        Response(OK).body(asset.readText())
                    }

                )
            )

}

private fun List<Fruit>.toJson(): String =
    "[" + this.map {it.toJson()}.joinToString(",") + "]"

private fun Fruit.toJson(): String =
        """ { "id": "${this.id}", "name": "${this.name}" } """

private fun String.getName(): String = this.substring(9, this.length - 2)