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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewModel.AuthViewModel


@Composable
fun SignUpScreen(
    onNavigateToLogin : () ->Unit,
    authViewModel: AuthViewModel
)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    
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
            label = { Text(text = "Email")} ,
            modifier= Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password=it},
            label = { Text(text = "Password")} ,
            modifier= Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = firstname,
            onValueChange = {firstname=it},
            label = { Text(text = "FirstName")} ,
            modifier= Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = lastname,
            onValueChange = {lastname=it},
            label = { Text(text = "LastName")},
            modifier= Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.signUp(email,password,firstname,lastname)
            email=""
            password=""
            firstname=""
            lastname=""
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            Text(text = "SIGN UP")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Already Have an Account ? Log In", modifier = Modifier
            .clickable { onNavigateToLogin()})
    }
}

//@Composable
//@Preview
//fun Preview()
//{
//    SignUpScreen()
//}