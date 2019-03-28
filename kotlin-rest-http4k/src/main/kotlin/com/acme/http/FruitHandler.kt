package com.acme.http

import com.acme.model.FruitRepository
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

class FruitHandler(val fruits: FruitRepository): HttpHandler {

    override fun invoke(request: Request): Response =
        routes(
                "fruits" bind routes(
                    "/" bind GET to {


                        Response(OK).body( fruits.getAll().toString())
                    },
                    "/{id}" bind GET to {
                        val fruit = fruits.getById(it.path("id")?.toIntOrNull() ?: 0)

                        Response(OK).body(fruit.toString())
                    }

                )
            )(request)
        }


