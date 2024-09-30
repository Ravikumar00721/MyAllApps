package com.example.plugins

import com.example.sendNotification
import io.ktor.serialization.gson.gson
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.routing

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson() // This ensures Ktor can deserialize JSON
    }
    routing {
        sendNotification() // Ensure this is called
    }
}
