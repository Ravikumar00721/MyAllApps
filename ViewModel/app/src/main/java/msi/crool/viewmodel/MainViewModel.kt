package msi.crool.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel(InitialCount:Int) : ViewModel() {
     private val _count= mutableStateOf(InitialCount)
    //we are not using this yet but when you want to access outside this then we use count
     val count :MutableState<Int> = _count
    fun increment()
    {
        _count.value++;
    }
    fun decrement()
    {
        _count.value--;
    }
}