package msi.crool.globofly.Services

import com.smartherd.globofly.models.Destination
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface DestinationServices {
    @Headers("x-device-type:Android","x-foo:bar")
    @GET("destination")
    fun getDestinationList(@QueryMap filter:HashMap<String,String>,@Header("Accept-Language")language:String): Call<List<Destination>>

    @GET("destination/{id}")
    fun getDestination(@Path("id") id: Int): Call<Destination>

    @POST("destination")
    fun addDestination(@Body newDestination:Destination):Call<Destination>

    @FormUrlEncoded
    @PUT("destination/{id}")
    fun updateDestination(
        @Path("id")id:Int,
        @Field("city")city:String,
        @Field("description")desc:String,
        @Field("country")country:String):Call<Destination>

    @DELETE("destination/{id}")
    fun deleteDestination(@Path("id")id:Int):Call<Unit>

}