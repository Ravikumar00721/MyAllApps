package msi.crool.globofly.Services

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

object ServiceBuilder {
    private const val URL="http://10.0.2.2:9000"

    val headerInterceptor:Interceptor=object :Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            var request:Request=chain.request()
            request= request.newBuilder()
                .addHeader("x-device-type",Build.DEVICE)
                .addHeader("Accept-lang",Locale.getDefault().language)
                .build()
            val response:Response=chain.proceed(request)
            return response
        }
    }

    private val okHttp : OkHttpClient.Builder=OkHttpClient.Builder().addInterceptor(
        headerInterceptor)
    private val builder: Retrofit.Builder=Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create()).client(okHttp.build())

    private val retrofit:Retrofit= builder.build()

    fun <T> buildService(serviceType: Class<T>) :T {
        return retrofit.create(serviceType)
    }
}