package com.example.googlesignin.Model

data class SignInResult(
    val data:userData?,
    val errorMessage:String?
)

data class userData(
    val userId:String?,
    val username:String?,
    val profilePicture:String?
)
