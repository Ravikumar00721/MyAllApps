package msi.crool.navigationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import msi.crool.navigationapp.ui.theme.NavigationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                         MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp()
{
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = "firstscreen"){
       composable("firstscreen"){
           FirstScreen {name,age->
               navController.navigate("secondscreen/$name/$age")
           }
       }
        composable("secondscreen/{name}/{age}") {
            val name=it.arguments?.getString("name")?:"no name"
            val age=it.arguments?.getString("age")?:"No age"
            SecondScreen(name,age
            )
            {
                navController.navigate("firstscreen")
            }
        }

        composable("thirdscreen"){
            ThirdScreen {
                navController.navigate("secondscreen")
            }
        }
    }
}
