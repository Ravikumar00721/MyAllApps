package msi.crool.kotlinbasics

fun main()
{
//    println("Enter Spoons of sugar")
//    val sc= readln().toInt()
//    println("Enter Name of Person")
//    val name= readln()
//    makeCoffee(sc,name)
    println("Enter Number 1 :")
    val num1= readln().toInt()
    println("Enter Number 2 :")
    val num2= readln().toDouble()
    val result=add(num1,num2)
    println(result)

}
fun makeCoffee(SC:Int,Name:String)
{
    if(SC==1)
    {
        println("$SC spoon of sugar ,Which is for $Name")
    }
    else
    {
        println("$SC spoons of sugar ,Which is for $Name")
    }
}
fun add(A:Int,B:Double):Double{
    return A/B
}