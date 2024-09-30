package msi.crool.countermvvm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CounterViewModel(): ViewModel() {
    private val repository: counterRepository=counterRepository()
    private val _count= mutableStateOf(repository.getCounter().count);

    //expose the count  as an  immutable state
    val count: MutableState<Int> = _count;
    fun increment()
    {
        repository.increment()
        _count.value=repository.getCounter().count;
    }
    fun decrement()
    {
        repository.decrement()
        _count.value=repository.getCounter().count
    }
}