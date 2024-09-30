package Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import viewModel.AuthViewModel
import data.Result

@Composable
fun LoginScreen(
    onNavigateToSignup : ()->Unit,
    authViewModel: AuthViewModel,
    onSignInSucces: () -> Unit
)
{
    val result by authViewModel.authResult.observeAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column (
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = {email=it},
            label = { Text(text = "Email") } ,
            modifier= Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password=it},
            label = { Text(text = "Password") } ,
            modifier= Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.login(email, password)
            when (result) {
                is Result.Success->{
                    onSignInSucces()
                }
                is Result.Error ->{

                }

                else -> {

                }
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            Text(text = "LOG IN")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Don't Have an Account ? Sign Up", modifier = Modifier
            .clickable {onNavigateToSignup()})
    }
}

//@Composable
//@Preview
//fun LoginPreview()
//{
//    LoginScreen()
//}