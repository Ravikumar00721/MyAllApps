package msi.crool.musicapp

import androidx.annotation.DrawableRes


sealed class Screen(val title:String,val route:String) {

    sealed class BottomScreen(val bTitle:String,val bRoute:String,@DrawableRes val icon:Int):Screen(bTitle,bRoute)
    {
        object Home:BottomScreen("Home","home",R.drawable.r_home)
        object Library:BottomScreen("Library","library",R.drawable.r_music)
        object Browse:BottomScreen("Browse","browse",R.drawable.r_browse)
    }

    sealed class DrawerScreen(val dTitle:String,val dRoute:String,@DrawableRes val icon:Int):Screen(dTitle,dRoute)
    {
        object Account:DrawerScreen(
            dTitle = "Account",
            dRoute = "account",
            R.drawable.r_account
        )
        object Subscription:DrawerScreen(
            dTitle = "Subscription",
            dRoute = "subscription",
            R.drawable.r_music
        )
        object AddAccount:DrawerScreen(
            dTitle = "AddAccount",
            dRoute = "add_account",
            R.drawable.icc_account
        )
    }
}
val drawerInScreen= listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.AddAccount,
    Screen.DrawerScreen.Subscription)
val KingBowser= listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Library,
    Screen.BottomScreen.Browse
)