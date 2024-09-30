package msi.crool.whislistapp

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SubscriptionView()
{
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Manage Subscription")
        Divider()
        Row (
            modifier= Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row {
                Column {
                    Text("Musical")
                    Text("Free Tier")
                }
                Text("See All Plans")
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription =null)
            }
        }
        Divider()
        Row (
            modifier= Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
           Icon(painter = painterResource(id = R.drawable.ic_account),
               contentDescription =null,
               modifier=Modifier.padding(8.dp))
            Text("Get a Plan")

        }
        Divider()
    }
}