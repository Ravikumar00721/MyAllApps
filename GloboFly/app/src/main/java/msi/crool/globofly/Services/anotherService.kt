package msi.crool.globofly.Services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface anotherService {
    @GET
    fun getURL(@Url anotherUrl:String): Call<String>
}