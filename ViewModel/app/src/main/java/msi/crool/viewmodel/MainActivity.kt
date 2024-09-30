package msi.crool.viewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import msi.crool.viewmodel.ui.theme.ViewModelTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val initialCount = 10
//            val viewModelFactory = MainViewModelFactory(initialCount)

            // Create the ViewModel using the factory
            val mainViewModel = ViewModelProvider(this, MainViewModelFactory(10)).get(MainViewModel::class.java)
            ViewModelTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                     CounterApp(mainViewModel)
                }
            }
        }
    }
}
@Composable
fun CounterApp(viewModel: MainViewModel)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Count is : ${viewModel.count.value}")
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(onClick = {viewModel.increment()}) {
                Text(text = "Increment")
            }
            Button(onClick = {viewModel.decrement()}) {
                Text(text = "Decrement")
            }
        }
    }
}
