package msi.crool.myshoppinglist

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    fun create(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("maps/api/geocode/json?")
    suspend fun getAddressfromCordinates(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String
    ): GeoCodingResponse
}

