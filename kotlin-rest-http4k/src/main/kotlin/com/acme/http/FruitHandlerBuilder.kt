package com.acme.http

import com.acme.model.Fruit
import com.acme.model.FruitRepository
import com.acme.model.toJson
import com.acme.model.toName
import io.undertow.Handlers.header
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
import javax.ws.rs.core.MediaType

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
                                    .header("Content-Type", MediaType.APPLICATION_JSON)
                        },
                        "/" bind POST to {
                            val fruitName = it.bodyString().toName()
                            if (!fruitName.isBlank())
                                fruits.addFruit(fruitName)
                            Response(CREATED)
                        },
                        "/{id}" bind GET to {
                            val fruit = fruits.getById(id(it))
                            Response(OK).body(fruit?.toJson().orEmpty())
                                    .header("Content-Type", MediaType.APPLICATION_JSON)
                        },
                        "/{id}" bind PUT to {
                            val fruit = Fruit(id(it), it.bodyString().toName())
                            fruits.replace(fruit)

                            Response(OK).body(fruit.toJson())
                                    .header("Content-Type", MediaType.APPLICATION_JSON)
                        },
                        "/{id}" bind DELETE to {
                            fruits.remove(id(it))
                            Response(NO_CONTENT)
                        }
                    ),
                    "/" bind GET to {
                        val asset = File("../../public/static/index.html") //it runs inside target/classes
                        Response(OK).body(asset.readText())
                                .header("Content-Type", MediaType.TEXT_HTML)
                    }

                )
            )

}

