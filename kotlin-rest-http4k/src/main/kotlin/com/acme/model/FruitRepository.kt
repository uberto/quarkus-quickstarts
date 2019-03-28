package com.acme.model

import java.util.concurrent.atomic.AtomicInteger

class FruitRepository {
    private val fruits = mutableListOf<Fruit>()

    private val nextId = AtomicInteger(1)

    fun addFruit(name: String) =
        Fruit(nextId.getAndIncrement(), name)
                .also { fruits.add(it) }


}
