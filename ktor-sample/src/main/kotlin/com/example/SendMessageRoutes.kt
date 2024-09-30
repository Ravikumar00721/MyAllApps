package com.example

import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.sendNotification() {
    route("/send") {
        post {
            // Print the receipt of a request
            println("Received POST request at /send")

            // Receive the request body as SendMessageDto
            val body = call.receiveNullable<SendMessageDto>() ?: run {
                println("Invalid or missing body in request")
                call.respond(HttpStatusCode.BadRequest, "Invalid or missing body")
                return@post
            }

            // Print the deserialized request body
            println("Received request body: $body")

            try {
                // Send the notification via FirebaseMessaging
                val messageId = FirebaseMessaging.getInstance().send(body.toMessage())

                // Print the message ID for successful delivery
                println("Notification sent successfully with ID: $messageId")
                call.respond(HttpStatusCode.OK, "Notification sent with ID: $messageId")
            } catch (e: Exception) {
                // Print the exception details
                println("Failed to send notification: ${e.message}")
                e.printStackTrace() // Print stack trace for debugging
                call.respond(HttpStatusCode.InternalServerError, "Failed to send notification: ${e.message}")
            }
        }
    }
}
