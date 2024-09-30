package msi.crool.musicapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AccountView()
{
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (
            modifier= Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (){
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription =null,modifier= Modifier.padding(end = 8.dp) )
                Column {
                    Text("PanjuTutorials")
                    Text("@tutorialsEU")
                }
            }
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
        Row(
            modifier= Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(painter = painterResource(id = R.drawable.r_account), contentDescription =null ,
                modifier= Modifier.padding(8.dp))
            Text("My Music")
        }
        Divider()
    }
}