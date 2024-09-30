package msi.crool.myshoppinglist

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApiService {
    @GET("maps/api/geocode/json?")
    suspend fun getAddressfromCordinates(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String
    ):GeoCodingResponse
}