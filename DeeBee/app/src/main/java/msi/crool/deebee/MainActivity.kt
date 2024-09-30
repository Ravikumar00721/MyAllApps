package msi.crool.deebee

import Screen.ChatRoomListScreen
import Screen.ChatScreen
import Screen.LoginScreen
import Screen.SignUpScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import msi.crool.deebee.ui.theme.DeeBeeTheme
import viewModel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeeBeeTheme {
                val navHostController = rememberNavController()
                val authViewModel: AuthViewModel = AuthViewModel()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navHostController = navHostController, authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navHostController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.SignupScreen.route,
        builder = {
            composable(Screen.SignupScreen.route) {
                SignUpScreen(authViewModel = authViewModel, onNavigateToLogin = { navHostController.navigate(Screen.LoginScreen.route) })
            }
            composable(Screen.LoginScreen.route) {
                LoginScreen(authViewModel = authViewModel, onNavigateToSignup = { navHostController.navigate(Screen.SignupScreen.route) }) {
                    navHostController.navigate(Screen.ChatRoomsScreen.route)
                }
            }
            composable(Screen.ChatRoomsScreen.route) {
                ChatRoomListScreen { room ->
                    navHostController.navigate("${Screen.ChatScreen.route}/${room.id}")
                }
            }
            composable("${Screen.ChatScreen.route}/{roomId}") { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                ChatScreen(roomId = roomId)
            }
        }
    )
}




