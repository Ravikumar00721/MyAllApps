package msi.crool.whislistapp

import androidx.annotation.DrawableRes

sealed class Screen(val title:String,val route:String) {
    sealed class DrawerScreen(val dTitle:String,val dRoute:String,@DrawableRes val icon:Int):Screen(dTitle,dRoute)
    {
       object Account:DrawerScreen(
           dTitle = "Account",
           dRoute = "account",
           R.drawable.ic_account
       )
        object Subscription:DrawerScreen(
            dTitle = "Subscription",
            dRoute = "subscription",
            R.drawable.ic_subscription
        )
        object AddAccount:DrawerScreen(
            dTitle = "AddAccount",
            dRoute = "add_account",
            R.drawable.ic_add_account
        )
    }
}
val drawerInScreen= listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.AddAccount,
    Screen.DrawerScreen.Subscription)