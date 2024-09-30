package msi.crool.kotlinbasics

fun main(){
    var Daisy=Dog("Daisy","Dwarf",1)
    println("${Daisy.name} breeed is ${Daisy.breed}  ${Daisy.age}" )
    println("Year Passed")
    Daisy.age=3
    println("${Daisy.name} breeed is ${Daisy.breed}  ${Daisy.age}" )
    println(Daisy.hashCode())
}