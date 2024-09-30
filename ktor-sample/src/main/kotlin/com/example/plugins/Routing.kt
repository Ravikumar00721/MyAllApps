package com.example.plugins

import com.example.SendMessageDto

import com.google.gson.Gson
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.example.sendNotification
import com.example.toMessage
import com.google.firebase.messaging.FirebaseMessaging

fun Application.configureRouting() {
    routing {
        route("/send") {
            post {
                val requestBody = call.receiveText() // Get the raw JSON
                println("Received JSON: $requestBody") // Print it to logs

                // Deserialize manually to check for errors
                try {
                    val messageDto = Gson().fromJson(requestBody, SendMessageDto::class.java)
                    println("Deserialized Object: $messageDto")

                    // Send the notification via FirebaseMessaging
                    try {
                        val messageId = FirebaseMessaging.getInstance().send(messageDto.toMessage())
                        println("Notification sent successfully with ID: $messageId")
                        call.respond(HttpStatusCode.OK, "Notification sent with ID: $messageId")
                    } catch (e: Exception) {
                        println("Failed to send notification: ${e.message}")
                        e.printStackTrace() // Print stack trace for debugging
                        call.respond(HttpStatusCode.InternalServerError, "Failed to send notification: ${e.message}")
                    }
                } catch (e: Exception) {
                    println("Deserialization Error: ${e.message}")
                    call.respond(HttpStatusCode.BadRequest, "Deserialization Error")
                }
            }
        }
    }
}


