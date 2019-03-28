package com.acme.http

import com.acme.model.FruitRepository
import org.http4k.core.*
import java.io.IOException
import java.util.*
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/")
open class RestServlet : HttpServlet() {

    val fruits = FruitRepository().apply {
        addFruit("Cherry")
        addFruit("Apple")
        addFruit("Banana")
    }

    val handler = FruitHandler(fruits)


    @Throws(IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {

        println("${System.currentTimeMillis()} serving ${request.requestURL}")

        handler(request.asHttp4kRequest()).transferTo(response)

        println("${System.currentTimeMillis()} done ${request.requestURL} with ${response.status}")
    }



    @Suppress("DEPRECATION")
    private fun Response.transferTo(destination: HttpServletResponse) {
        destination.setStatus(status.code, status.description)
        headers.forEach { (key, value) -> destination.addHeader(key, value) }
        body.stream.use { input -> destination.outputStream.use { output -> input.copyTo(output) } }
    }

    private fun HttpServletRequest.asHttp4kRequest(): Request =
            Request(Method.valueOf(method), Uri.of(requestURI + queryString.toQueryString()))
                    .body(inputStream, getHeader("Content-Length").safeLong()).headers(headerParameters())

    private fun HttpServletRequest.headerParameters(): Headers =
            headerNames.asSequence().fold(listOf()) { a: Parameters, b: String -> a.plus(getHeaders(b).asPairs(b)) }

    private fun Enumeration<String>.asPairs(key: String): Parameters = asSequence().map { key to it }.toList()

    private fun String?.toQueryString(): String = if (this != null && this.isNotEmpty()) "?" + this else ""

}