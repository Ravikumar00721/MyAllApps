package msi.crool.toketo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import msi.crool.toketo.ui.theme.LoginScreen
import msi.crool.toketo.ui.theme.ToketoTheme
import screen.ChatScreen
import screen.Screen
import screen.SignUpScreen
import viewModal.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController= rememberNavController()
            val authViewModel:AuthViewModel= viewModel()
//            val roomViewModel : RoomViewModel = viewModel()
            ToketoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                     NavigationGraph(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }
}
@Composable
fun NavigationGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel=authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) }
            )
        }
        composable(Screen.LoginScreen.route) {
           LoginScreen(authViewModel = authViewModel,
               onNavigateToSignUp = {navController.navigate(Screen.SignupScreen.route)}
               )
        }
        composable(Screen.ChatRoomsScreen.route)
        {
            navController.navigate("${Screen.ChatScreen.route}/${it.id}")
        }
        composable("${Screen.ChatScreen.route}/{roomId}") {
            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }
    }
}