package msi.crool.myrecipeapp
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Error

class MainViewModel:ViewModel() {
    private val _categoriesState= mutableStateOf(RecipeState())// mutable
    val categoryStates: State<RecipeState> = _categoriesState //read only ,immutable
    //State reflect the changes directly to UI
    //This means that external classes cannot directly access or modify the mutable state variable,
    // ensuring that the state remains controlled and consistent.

    init {
        fetchCategories()   //for calling
    }

    private fun fetchCategories()
    {
        //The launch coroutine builder is used to start a new coroutine within the viewModelScope.
        //When you call viewModelScope.launch { ... }, you're starting a new coroutine that runs asynchronously in the background
        viewModelScope.launch {
            try {
                val response= recipieService.getCategories()
                _categoriesState.value=_categoriesState.value.copy(
                    list=response.categories,
                    loading = false,
                    error = null
                )

            }catch (e:Exception)
            {
                _categoriesState.value=_categoriesState.value.copy(
                    loading = false,
                    error="Error Fetching Categories ${e.message}"
                )
            }
        }
    }

    data class RecipeState(
        val loading:Boolean=true,
        val list:List<Category> = emptyList(),
        val error: String?=null
    )

}