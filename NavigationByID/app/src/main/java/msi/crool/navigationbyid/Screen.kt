package msi.crool.navigationbyid

sealed class Screen (val route:String){
    object Room1: Screen("ChatRoomScreen")
}