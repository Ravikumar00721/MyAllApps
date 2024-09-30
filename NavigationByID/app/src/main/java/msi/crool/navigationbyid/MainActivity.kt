package msi.crool.navigationbyid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController= rememberNavController()
            MyApp(navController=navController)
        }
    }
}
@Composable
fun MyApp(navController: NavController) {
   NavHost(navController = navController as NavHostController, startDestination = Screen.Room1.route, builder = {
       composable(Screen.Room1.route)
       {
           ChatRoomListScreen{room->
               navController.navigate("chat/${room.id}")
           }
       }
       composable(
           "chat/{roomId}",
//           arguments = listOf(navArgument("roomId") { type = NavType.StringType })
       ) { backStackEntry ->
           val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
           ChatScreen(roomId = roomId)
       }
   })
}

@Composable
fun ChatRoomListScreen(onRoomClick: (ChatRoom) -> Unit) {
    val rooms = listOf(
        ChatRoom("1", "Room 1"),
        ChatRoom("2", "Room 2"),
        ChatRoom("3", "Room 3")
    )
    Column {
        rooms.forEach { room ->
            Button(onClick = { onRoomClick(room) }) {
                Text(text = room.name)
            }
        }
    }
}

@Composable
fun ChatScreen(roomId:String) {
    Text(text = "Chat Room ID: $roomId")
}