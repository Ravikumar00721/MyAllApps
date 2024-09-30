package com.example.notification

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.notification.ui.theme.NotificationTheme
import com.example.notification.viewmodel.ChatViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        setContent {
            NotificationTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                     val state=viewModel.state
                    if(state.isEnteringToken)
                    {
                        EnterTokenDialog(
                            token = state.remoteToken,
                            onTokenChange = viewModel::onRemoteChanged,
                            onSubmit = viewModel::onSubmitRemoteToken
                        )
                    }else
                    {
                        SendMessage(
                            mesaageText = state.messageString,
                            onMessageSend = {
                                viewModel.sendBroadM(isBroadCast = false)
                            },
                            onMessageBroadCast = {
                                viewModel.sendBroadM(isBroadCast = true)
                            },
                            onMessageChange = viewModel::onMessageChanged
                        )
                    }
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission() {
        val hasPer=ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )== PackageManager.PERMISSION_GRANTED
        if(!hasPer)
        {
            Log.d("NotificationPermission", "Requesting notification permission")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        else {
            Log.d("NotificationPermission", "Notification permission already granted")
        }
    }
}

