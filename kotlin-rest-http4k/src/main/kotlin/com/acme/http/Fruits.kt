package com.acme.http

import com.acme.model.FruitRepository


val fruits = FruitRepository().apply {
    addFruit("Cherry")
    addFruit("Apple")
    addFruit("Banana")
}

val fruitsHandler = FruitHandler(fruits).createHandler()