package data

data class Message(
    val text: String = "",
    val senderId: String = "",
    val senderFirstName: String = "",
    val timestamp: Long = 0L,
    var isSentByCurrentUser: Boolean = false
)
