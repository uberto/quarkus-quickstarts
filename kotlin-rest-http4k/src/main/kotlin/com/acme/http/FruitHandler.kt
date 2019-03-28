package com.acme.http

import com.acme.model.FruitRepository
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK

class FruitHandler(val fruits: FruitRepository): HttpHandler {

    override fun invoke(req: Request): Response {

        return Response(OK)
    }

}
