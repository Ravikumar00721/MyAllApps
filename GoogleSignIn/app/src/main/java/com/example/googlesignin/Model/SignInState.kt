package com.example.googlesignin.Model

data class SignInState(
    val isSignInSucessFull:Boolean=false,
    val signInError:String?=null
)