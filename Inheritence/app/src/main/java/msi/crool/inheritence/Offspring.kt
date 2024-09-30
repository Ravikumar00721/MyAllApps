package msi.crool.inheritence

class Offspring:Secondary(),Singer,Archery {
    override fun archery()
    {
           super.archery()
           println("This is archery skills enhanced")
    }
    override  fun sing()
    {
        super.sing()
        println("This is singing skill enhanced")
    }

}