package msi.crool.kotlinbasics

fun main()
{
    println("Enter Your Age :")
    val age=readln().toInt()
    if(age in 18..39)
    {
        println("You Are Young")
    }
    else if(age>=40)
    {
        println("You are too old")
    }
    else
    {
        println("Go Home Buddy")
    }

}