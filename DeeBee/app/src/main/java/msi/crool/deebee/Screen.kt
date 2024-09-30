package msi.crool.deebee

sealed class Screen(val route: String) {
    object LoginScreen : Screen("loginscreen")
    object SignupScreen : Screen("signupscreen")
    object ChatRoomsScreen : Screen("chatroomscreen")
    object ChatScreen : Screen("chatscreen/{roomId}")
}

