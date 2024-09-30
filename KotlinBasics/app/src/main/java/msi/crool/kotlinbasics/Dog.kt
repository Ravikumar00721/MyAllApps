package msi.crool.kotlinbasics

class Dog(val name:String,val breed:String,var age:Int=0) {
    init {
        Bark(name,breed)
    }
    fun Bark(name:String,breed: String)
    {
        println("$name says woof woof")
    }
}