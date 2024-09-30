package com.example.notification.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notification.model.NotificationBody
import com.example.notification.model.SendMessageDto
import com.example.notification.model.chatstate
import com.example.notification.services.FcmApi
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ChatViewModel : ViewModel() {

    var state by mutableStateOf(chatstate())
    private val api: FcmApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FcmApi::class.java)

    init{
        viewModelScope.launch {
            Firebase.messaging.subscribeToTopic("chat").await()
        }
    }
    fun onRemoteChanged(newToken:String)
    {
        state=state.copy(
            remoteToken = newToken
        )
        Log.d("ChatViewModel", "Remote token changed: $newToken")
    }

    fun onSubmitRemoteToken()
    {
        Log.d("ChatViewModel", "Submitting remote token: ${state.remoteToken}")
        state=state.copy(
            isEnteringToken = false
        )
    }

    fun onMessageChanged(newMessage:String)
    {
        state=state.copy(
            messageString = newMessage
        )
        Log.d("ChatViewModel", "Message changed: $newMessage")
    }
    fun sendBroadM(isBroadCast:Boolean)
    {
        Log.d("ChatViewModel", if (isBroadCast) "Broadcasting message" else "Sending direct message")
        viewModelScope.launch {
            val messageDto=SendMessageDto(
                to=if(isBroadCast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "New Message !!",
                    body=state.messageString
                )
            )
            try {
                if(isBroadCast)
                {
                    api.broadcastMessage(messageDto)
                }else
                {
                    api.sendMessage(messageDto)
                }

                state=state.copy(
                    messageString = ""
                )
            }catch (e:HttpException)
            {
                e.printStackTrace()
            }catch (e:IOException)
            {
                e.printStackTrace()
            }
        }

    }
}
