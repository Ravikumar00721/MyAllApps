package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import data.Result
import data.Room
import data.RoomRepository
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val repository = RoomRepository(FirebaseFirestore.getInstance())
    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms

    init {
        loadRooms()
    }

    private fun loadRooms() {
        viewModelScope.launch {
            when (val result = repository.getRooms()) {
                is Result.Success -> _rooms.value = result.data
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun createRoom(name: String) {
        viewModelScope.launch {
            when (val result = repository.createRoom(name)) {
                is Result.Success -> {
                    loadRooms() // Reload rooms to include the newly created one
                }
                is Result.Error -> {
                    // Handle failure
                }
            }
        }
    }
}
