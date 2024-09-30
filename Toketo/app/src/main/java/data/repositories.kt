package data

import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(firstName, lastName, email)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        return try {
            val uid = auth.currentUser?.email
            if (uid != null) {
                val userDocument = firestore.collection("users").document(uid).get().await()
                val user = userDocument.toObject(User::class.java)
                if (user != null) {
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
}


object Injection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun instance(): FirebaseFirestore {
        return instance
    }
}

