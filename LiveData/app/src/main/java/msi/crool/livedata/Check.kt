package msi.crool.livedata

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

fun fetchData():Result<String> {
   return try{
       val data="Hello World"
       Result.Success(data)
   }catch (e:Exception)
   {
       Result.Error(e)
   }
}

@Composable
fun handleData()
{
    when(val result= fetchData())
    {
        is Result.Success->
        {
            val data =result.data
            println("Data : $data")
            Screen(data)
        }
        is Result.Error->
        {
            val exception=result.exception
           println("Error is  : $exception")
        }
    }
}
@Composable
fun Screen(result: String)
{
    Button(onClick = { /*TODO*/ }) {
        Text(text = result)
    }
}