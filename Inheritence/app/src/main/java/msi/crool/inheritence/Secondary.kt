package msi.crool.inheritence

open class Secondary:BaseClass() {
    override fun role()
    {
        super.role()
        println("This is Core Values of DerivedClass")
    }
    fun behaviour()
    {
        println("This is Core Behaviour of DerivedClass")
    }
}