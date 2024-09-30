package msi.crool.todolist

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

data class ToDosItem(
    val id:Int,
    var name:String,
    var quantity:Int,
    var isEditing:Boolean=false
)

@Composable
fun TodosListApp()
{
    var sItems by remember{ mutableStateOf(listOf<ToDosItem>()) }
    var showDialog by remember{ mutableStateOf(false) }
    var itemName by remember{ mutableStateOf("") }
    var itemQuantity by remember{ mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {showDialog=true},
            modifier= Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "ADD")
        }
        LazyColumn(
            modifier= Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
          items(sItems)
          {
              item->
              if(item.isEditing)
              {
                  TodosItemEditor(item =item , onEditComplete =
                  {
                      editedName,editedQuantity->
                      sItems=sItems.map{it.copy(isEditing = false)}
                      val editeditem=sItems.find{it.id==item.id}
                      editeditem?.let{
                          it.name=editedName
                          it.quantity=editedQuantity
                      }
                  })
              }else{
                  TodosListItem(item = item, onEditClick = {
                      sItems=sItems.map{it.copy(isEditing = it.id==item.id)}
                  }) {
                      sItems=sItems-item;
                  }
              }
          }

        }
    }
    if(showDialog)
    {
        AlertDialog(
            onDismissRequest = { showDialog=false },
            confirmButton = { 
                            Row (modifier= Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                Button(onClick = {
                                    if(itemName.isNotBlank())
                                    {
                                        val newItem=ToDosItem(
                                            id=sItems.size+1,
                                            name=itemName,
                                            quantity=itemQuantity.toInt()
                                        )
                                        sItems=sItems+newItem
                                        showDialog=false
                                        itemName=""
                                        itemQuantity=""
                                    }
                                }) {
                                    Text(text = "Add")
                                }
                                Button(onClick = {showDialog=false}) {
                                    Text(text = "Cancel")
                                }
                            }
            },
            title = { Text(text = "Add Item")},
            text = {
              Column {
                  Text(text = "Enter Item Name")
                  OutlinedTextField(
                      value = itemName,
                      onValueChange ={itemName=it},
                      singleLine = true,
                      modifier= Modifier
                          .fillMaxWidth()
                          .padding(8.dp)
                      )
                  Text(text = "Enter Quantity")
                  OutlinedTextField(
                      value = itemQuantity,
                      onValueChange ={itemQuantity=it},
                      singleLine = true,
                      modifier= Modifier
                          .fillMaxWidth()
                          .padding(8.dp)
                  )
              }
            }
            )
    }
}
@Composable
fun TodosItemEditor(item:ToDosItem,onEditComplete:(String,Int)->Unit)
{
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) }
    var isEditing by remember{ mutableStateOf(item.isEditing) }
    Row (
        modifier= Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
       Column {
            BasicTextField(
                value =editedName,
                onValueChange = {editedName=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                )
           BasicTextField(
               value =editedQuantity,
               onValueChange = {editedQuantity=it},
               singleLine = true,
               modifier = Modifier
                   .wrapContentSize()
                   .padding(8.dp)
           )
           Button(onClick = {
               isEditing=false
               onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)
           }) {
              Text(text = "SAVE")
           }
       }
    }
}

@Composable
fun TodosListItem(item:ToDosItem,
                  onEditClick:()->Unit,
                  onDeleteClick:()->Unit)
{
   Row (
       modifier= Modifier
           .fillMaxWidth()
           .padding(8.dp)
           .border(
               border = BorderStroke(2.dp, Color(0XFF018786)),
               shape = RoundedCornerShape(20)
           ),
       horizontalArrangement = Arrangement.SpaceBetween
   ){
       Text(text = item.name,modifier= Modifier.padding(8.dp))        
       Text(text = "QTY" + item.quantity,modifier=Modifier.padding(8.dp))
       Row (modifier= Modifier.padding(8.dp)){
           IconButton(onClick = onEditClick ) {
               Icon(imageVector = Icons.Default.Edit,contentDescription = null)
           }
           IconButton(onClick = onDeleteClick) {
               Icon(imageVector = Icons.Default.Delete,contentDescription = null)
           }
       }
   }
}