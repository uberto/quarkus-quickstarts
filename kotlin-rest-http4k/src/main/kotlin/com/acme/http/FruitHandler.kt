package com.acme.http

import com.acme.model.Fruit
import com.acme.model.FruitRepository
import org.http4k.core.*
import org.http4k.core.Method.*
import org.http4k.core.Status.Companion.ACCEPTED
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.*
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.ResourceLoader.Companion.Directory
import java.io.FileInputStream

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
                                fruits.remove(it.path("id")?.toIntOrNull()?:0)
                                Response(ACCEPTED)
                            }

                    ),
                    "/{assetName}" bind GET to {

                        val resource = FruitHandler::class.java.getResource("/static/index.html")
                        Response(OK).body(resource.openStream())

//                        static(Classpath("static"))(it)
                    }

            )(request)
    }

