package msi.crool.kotlinbasics
data class Person(
    val name:String,
    val age:Int
        )
fun main()
{
    val Person1=Person("Ravi Kumar",21)
    Per(Person1)
}
fun Per(P:Person)
{
    println("My name is ${P.toString()}")
    val person3=P.copy(name="Mainsh Kumar")
    println(person3)
}