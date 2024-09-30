package msi.crool.navigationapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import msi.crool.navigationapp.ui.theme.NavigationAppTheme

@Composable
fun FirstScreen(navigationToSecondScreen:(String,String)->Unit)
{
    val name= remember {
        mutableStateOf("")
    }
    val age= remember {
        mutableStateOf("")
    }
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text ="This is First Screen")
        Spacer(modifier = Modifier.padding(16.dp))
        OutlinedTextField(value = name.value, onValueChange ={
            name.value=it;
        } )
        Spacer(modifier = Modifier.padding(16.dp))
        OutlinedTextField(value = age.value, onValueChange ={
            age.value=it;
        } )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = { navigationToSecondScreen(name.value,age.value) }) {
            Text(text = "Go to Second")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstPreview() {
    NavigationAppTheme {
        FirstScreen { _, _ -> } // Provide an empty lambda function since you're not using navigation here
    }
}
