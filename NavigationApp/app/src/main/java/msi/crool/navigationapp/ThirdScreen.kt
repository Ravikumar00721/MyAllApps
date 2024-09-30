package msi.crool.navigationapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun ThirdScreen(navigationTOSecondScreen:()->Unit)
{
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text ="This is Third Screen")
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "Welcome to Third Screen")
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = { navigationTOSecondScreen() }) {
            Text(text = "Go To Second")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThirdPreview() {
    NavigationAppTheme {
        ThirdScreen({})
    }
}