package msi.crool.saptap.Services

import msi.crool.saptap.Model.Speeches
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SpeechService {
    @POST("/chat")
    fun addDestination(@Body speech: Map<String, String>): Call<Speeches>
}
