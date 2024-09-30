package com.example

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody
)
data class NotificationBody(
    val title: String,
    val body: String
)

fun SendMessageDto.toMessage(): Message {
    return Message.builder()
        .setToken(this.to) // This is the device token
        .setNotification(
            Notification.builder()
                .setTitle(this.notification.title)
                .setBody(this.notification.body)
                .build()
        )
        .build()
}

