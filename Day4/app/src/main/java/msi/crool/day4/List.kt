package msi.crool.day4

fun main()
{
    val things= listOf("RTX","Cooling System","64 GB RAM")
    val things2= mutableListOf("RTX","Cooling System","RTX","64 GB RAM",98)
    println(things2)
    things2.add(2,"Cabinet")
    things2.removeAt(3)
    println(things2)
    println(things2[2])
    things2[3]="102 RAM"
    things2.set(1,"RTX 4080")
    val hasContain=things2.contains("RTX")
    if(hasContain)
    {
        println("Yes it Contain")
    }
    else
    {
        println("Otherwise Not")
    }
//    println(things2)
    for(index in 2 until things2.size )
    {
        println("Item ${things2[index]} at index $index")
//        if(items=="RTX")
//        {
//            things2.removeFirst()
//            break
//        }
    }
//    for(index in 2 until 4 )
//    for(index in 2..4 )
//    println(things2)
}