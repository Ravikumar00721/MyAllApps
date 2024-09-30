package msi.crool.navigationapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import msi.crool.navigationapp.ui.theme.NavigationAppTheme

@Composable
fun SecondScreen(name:String, age:String, navigationToFirstScreen: () -> Unit)
{
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text ="This is Second Screen")
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "Welcome to Second Screen $name and age is $age")
        Spacer(modifier = Modifier.padding(8.dp))
        Row {
            Button(onClick = {navigationToFirstScreen() }) {
                Text(text = "Go To First")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SecondPreview() {
    NavigationAppTheme {
        SecondScreen("Ravi", "") {} 
    }
}
