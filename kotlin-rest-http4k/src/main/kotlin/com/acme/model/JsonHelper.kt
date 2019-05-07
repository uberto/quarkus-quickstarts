package com.acme.model

import java.io.StringReader
import java.io.StringWriter
import javax.json.Json
import javax.json.stream.JsonGenerator

private fun JsonGenerator.fruit(fruit: Fruit): JsonGenerator {
    this.writeStartObject()
            .write("id", fruit.id)
            .write("name", fruit.name)
            .writeEnd()
    return this
}

fun Fruit.toJson(): String {
    val writer = StringWriter()
    Json.createGenerator(writer).use {
        it.fruit(this)

    }
    return writer.toString()
}

fun List<Fruit>.toJson(): String {
    val writer = StringWriter()
    Json.createGenerator(writer).use {generator ->
                generator.writeStartArray()
                this.forEach {
                    generator.fruit(it)
                }
                generator.writeEnd()
    }
    return writer.toString()
}

fun String.toFruit(): Fruit =
    Json.createReader(StringReader(this)).use {
        val obj = it.readObject()
         Fruit(obj.getInt("id"), obj.getString("name"))
    }

fun String.toName(): String =
        Json.createReader(StringReader(this)).use {
            val obj = it.readObject()
            obj.getString("name")
        }


