package msi.crool.myrecipeapp

sealed class Screen(val route:String) {
   object RecipieScreen:Screen("receipiescreen")
    object DetailScreen:Screen("detailscreen")
 }