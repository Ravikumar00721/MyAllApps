package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import data.Message
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages
    val currentUser = MutableLiveData(FirebaseAuth.getInstance().currentUser)

    fun loadMessages(roomId: String) {
        firestore.collection("ChatRooms")
            .document(roomId)
            .collection("Messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val messageList = mutableListOf<Message>()
                if (snapshot != null) {
                    for (doc in snapshot.documents) {
                        val message = doc.toObject(Message::class.java)
                        if (message != null) {
                            messageList.add(message)
                        }
                    }
                }
                _messages.value = messageList
            }
    }

    fun sendMessage(roomId: String, text: String) {
        val user = currentUser.value
        if (user != null) {
            val message = Message(
                text = text,
                senderId = user.email ?: "",
                senderFirstName = user.displayName ?: "",
                timestamp = System.currentTimeMillis(),
                isSentByCurrentUser = true
            )
            firestore.collection("ChatRooms")
                .document(roomId)
                .collection("Messages")
                .add(message)
                .addOnSuccessListener {
                    loadMessages(roomId) // Reload messages after sending
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }
    }
}
