package data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            saveUserToFirestore(User(firstName, lastName, email))
            Log.d("UserRepository", "Sign-up successful for email: $email")
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("UserRepository", "Sign-up failed", e)
            Result.Error(e)
        }

    private suspend fun saveUserToFirestore(user: User) {
        try {
            firestore.collection("UsersLog").document(user.email).set(user).await()
            Log.d("UserRepository", "User saved to Firestore: ${user.email}")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error saving user to Firestore", e)
            throw e // Re-throw exception to propagate the error
        }
    }

    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d("UserRepository", "Login successful for email: $email")
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("UserRepository", "Login failed", e)
            Result.Error(e)
        }
    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()
            val user = userDocument.toObject(User::class.java)
            if (user != null) {
                Log.d("user2","$uid")
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
}
