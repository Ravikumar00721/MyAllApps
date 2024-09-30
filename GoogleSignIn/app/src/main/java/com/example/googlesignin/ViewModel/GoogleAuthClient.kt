package com.example.googlesignin.ViewModel

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.googlesignin.Model.SignInResult
import com.example.googlesignin.Model.userData
import com.example.googlesignin.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthClient (
    private val context:Context,
    private val oneTapClient:SignInClient
){
    private val auth=Firebase.auth

    suspend fun signIn():IntentSender?{
        val result=try {
            oneTapClient.beginSignIn(
               buildsignInRequest()
            ).await()
        }catch (e:Exception)
        {
            e.printStackTrace()
            if( e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()

            auth.signOut()

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }


    fun getSignedInUser():userData?=auth.currentUser?.run {
        userData(
            userId = uid,
            username = displayName,
            profilePicture = photoUrl?.toString()
        )
    }

    suspend fun signInWithIntent(intent: Intent):SignInResult{
        val credential=oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken=credential.googleIdToken
        val googleCredential=GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
             val user=auth.signInWithCredential(googleCredential).await().user
            SignInResult(
                data = user?.run {
                    userData(
                        userId = uid,
                        username = displayName,
                        profilePicture = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        }catch (e:Exception)
        {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    private fun buildsignInRequest():BeginSignInRequest
    {
       return BeginSignInRequest.builder()
           .setGoogleIdTokenRequestOptions(
               GoogleIdTokenRequestOptions.builder()
                   .setSupported(true)
                   .setFilterByAuthorizedAccounts(false)
                   .setServerClientId(context.getString(R.string.SignID))
                   .build()
           )
           .setAutoSelectEnabled(true)
           .build()
    }
}