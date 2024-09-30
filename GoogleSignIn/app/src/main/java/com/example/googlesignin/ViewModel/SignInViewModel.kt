package com.example.googlesignin.ViewModel

import androidx.lifecycle.ViewModel
import com.example.googlesignin.Model.SignInResult
import com.example.googlesignin.Model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel:ViewModel() {
    private val _state= MutableStateFlow(SignInState())
    val state=_state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update {
            it.copy(
                isSignInSucessFull = result.data!=null,
                signInError = result.errorMessage
            )
        }
    }
    fun resetState()
    {
        _state.update { SignInState() }
    }
}