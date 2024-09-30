package msi.crool.kotlinbasics

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

fun doomdam()
{
    val arraylist=ArrayList<String>(5)
    var listt= mutableListOf<String>()
    listt.add("Ravi")
    listt.add("Manish")
    listt.add("Hasim")
//            arraylist.add("One")
//            arraylist.add("Seven")
    arraylist.addAll(listt)
    for(i in arraylist)
    {
        println(i)
    }
}
@Composable
@Preview (showBackground = false)
fun funPreview(){
    doomdam()
}