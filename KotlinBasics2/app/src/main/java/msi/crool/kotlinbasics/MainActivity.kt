package msi.crool.kotlinbasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import msi.crool.kotlinbasics.ui.theme.KotlinBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinBasicsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val a=2;
//                    val b=4;
//                    if(a<b)
//                    {
//                        println("A is greater than b")
//                    }
//                    else
//                    {
//                        println("B is greater than a")
//                    }


//                    val numbers:IntArray= intArrayOf(1,2,3,4,5,6,7)
//                    val numbers= intArrayOf(1,2,3,4,5,6,7)
//                    val numbers= arrayOf(1,2,3,4,5,6,7,"R")
//                    numbers[0]=7
//                    print(numbers.contentToString())
//                    for(elements in numbers)
//                    {
//                        println("$elements +2")
//                    }
//                    val numbers2= doubleArrayOf(1.0,8.9)
//                    println(numbers2.contentToString())
//                    data class Fruit(val name:String,val price:Int)
//                    val fruits= arrayOf(Fruit("Grapes",40),Fruit("Apple",70))
//                    for(fruit in fruits.indices)
//                    {
//                        println("${fruits[fruit].name} $fruit")
//                    }

                    val months= listOf(1,2,3,4,'R')
//                    println(months[1])
//                    for (m in months)
//                    {
//                        println(m)
//                    }
//                    val additionalMonths=months.toMutableList()
//                    additionalMonths.add(7)
//                    for (m in additionalMonths)
//                    {
//                        println(m)
//                    }
//                   val removelist= mutableListOf<String>("Sunday","Monday","Tuesday")
//                    val rr= arrayOf("Friday","Tuesday")
////                    removelist.addAll(rr)
//                    removelist.removeAll(rr)
//                    for(m in removelist)
//                    {
//                        println(m)
//                    }

//                    val fruits= setOf("Oragnge","Mango","Apple","Apple")
//                    println(fruits.toSortedSet())
//                    println(fruits.size)
//                    println(fruits.elementAt(2))

//
//                    data class F(val name:String,val price:Int)
//                    val fruits= mapOf(1 to "Apple",2 to "Grapes",3 to F("Mango",40))
//                    for(key in fruits.keys)
//                    {
//                        println("${fruits[3]}")
//                    }


//                    val arraylist=ArrayList<String>(5)
//                    var listt= mutableListOf<String>()
//
//                    listt.add("Ravi")
//                    listt.add("Manish")
//                    listt.add("Hasim")
////            arraylist.add("One")
////            arraylist.add("Seven")
//                    arraylist.addAll(listt)
////                    for(i in arraylist)
////                    {
////                        println(i)
////                    }
//                    val itr=arraylist.iterator()
//                    while(itr.hasNext())
//                    {
//                        println(itr.next())
//                    }
//                    println("Size of ArrayList : " + arraylist.size)
//                    println("Size of ArrayList : " + arraylist.get(1))

//                    val arrayList=ArrayList<Double>()
//                    var list= mutableListOf<Double>()
//                    list.add(1.7)
//                    list.add(2.2)
//                    list.add(3.5)
//                    list.add(4.5)
//                    list.add(1.5)
//                    arrayList.addAll(list)
//                    var num = 0.0
//                    for(i in arrayList)
//                    {
//                       num+=i
//                    }
//                    var avg=num/arrayList.size
//                    println("Avg is $avg")

                    val sum:(Int,Int)->Int={a:Int,b:Int->a+b}
                    println(sum(3,4))
                    val summ={a:Int,b:Int-> println(a+b) }
                }
            }
        }
    }
}

