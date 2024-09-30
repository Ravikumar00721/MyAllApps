package com.example.notification.services

import com.example.notification.model.SendMessageDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )

    @POST("/broadcast")
    suspend fun broadcastMessage(
        @Body body: SendMessageDto
    )
}
