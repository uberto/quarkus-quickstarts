package com.acme.http

import com.acme.model.Fruit
import com.acme.model.FruitRepository
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Response
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.Path
import org.http4k.lens.int
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

object FruitHandler {
    private val id = Path.int().of("id")

    operator fun invoke(fruits: FruitRepository) =
        ServerFilters.CatchLensFailure
            .then(
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
                            val fruit = fruits.getById(id(it))
                            Response(OK).body(fruit.toString())
                        },
                        "/{id}" bind PUT to {
                            val fruit = Fruit(id(it), it.bodyString())
                            fruits.replace(fruit)

                            Response(OK).body(fruit.toString())
                        },
                        "/{id}" bind DELETE to {
                            fruits.remove(id(it))
                            Response(ACCEPTED)
                        }
                    ),
                    static(Classpath("static"))
                )
            )
}