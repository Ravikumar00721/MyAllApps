package msi.crool.myrecipeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun ReceipieApp(navController: NavHostController)
{
    val receipieModel:MainViewModel= viewModel()
    val viewstate by receipieModel.categoryStates
    
    NavHost(navController = navController, startDestination = Screen.RecipieScreen.route)
    {
        composable(route=Screen.RecipieScreen.route)
        {
            ReceipieScreen(viewstate = viewstate, navigateToDetail ={
                navController.currentBackStackEntry?.savedStateHandle?.set("cat",it)
                navController.navigate(Screen.DetailScreen.route)
            } )
        }
        composable(route=Screen.DetailScreen.route)
        {
            val category=navController.previousBackStackEntry?.savedStateHandle?.get<Category>("cat")?:
            Category("","","","")
            CategoryDeatilsScreen(category = category)
        }
    }
}