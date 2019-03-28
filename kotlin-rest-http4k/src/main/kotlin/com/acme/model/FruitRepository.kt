package com.acme.model

import java.util.concurrent.atomic.AtomicInteger

class FruitRepository {
    private val fruits = mutableListOf<Fruit>()

    private val nextId = AtomicInteger(1)

    fun addFruit(name: String): Fruit =
        Fruit(nextId.getAndIncrement(), name)
                .also { fruits.add(it) }

    fun getAll() = fruits.sortedBy { it.name }
    fun getById(id: Int) = fruits.firstOrNull() { it.id == id }

}
