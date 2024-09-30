package com.example.googlesignin.ViewModel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.googlesignin.Model.userData
import com.example.googlesignin.View.SignInScreen
import com.example.googlesignin.ui.theme.GoogleSignInTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleSignInTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            // Navigate to profile if already signed in
                            LaunchedEffect(key1 = Unit) {
                                googleAuthClient.getSignedInUser()?.let {
                                    navController.navigate("profile")
                                }
                            }

                            // Handle Sign-In Intent
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            // Handle sign-in success and navigate to profile
                            LaunchedEffect(key1 = state.isSignInSucessFull) {
                                if (state.isSignInSucessFull) {
                                    Toast.makeText(applicationContext, "Sign In Successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate("profile") {
                                        popUpTo("sign_in") { inclusive = true }
                                    }
                                    viewModel.resetState()
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthClient.signIn()
                                        signInIntentSender?.let {
                                            launcher.launch(
                                                IntentSenderRequest.Builder(it).build()
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        // Profile Screen
                        composable("profile") {
                            val currentUser = googleAuthClient.getSignedInUser()

                            if (currentUser != null) {
                                ProfileScreen(
                                    userData = currentUser,
                                    onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthClient.signOut()
                                            Toast.makeText(applicationContext, "Sign Out Successful", Toast.LENGTH_SHORT).show()
                                            navController.navigate("sign_in") {
                                                popUpTo("sign_in") { inclusive = true }
                                            }
                                        }
                                    }
                                )
                            } else {
                                LaunchedEffect(Unit) {
                                    navController.navigate("sign_in") {
                                        popUpTo("sign_in") { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
