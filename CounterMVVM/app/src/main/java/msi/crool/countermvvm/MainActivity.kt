package msi.crool.countermvvm

import android.os.Bundle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import msi.crool.countermvvm.ui.theme.CounterMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: CounterViewModel = viewModel();
            CounterMVVMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                     TheCounterApp(viewModel);
                }
            }
        }
    }
}
@Composable
fun TheCounterApp(viewMod: CounterViewModel)
{
    val count =remember{ mutableStateOf(0) }

    Column(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
         Text(text = "Count :${viewMod.count.value}",
             fontSize = 24.sp,
             fontWeight = FontWeight.Bold
             )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {viewMod.increment()}) {
                Text(text = "Increment")
            }
            Button(onClick = {viewMod.decrement()}) {
                Text(text = "Decrement")
            }
        }

    }
}
