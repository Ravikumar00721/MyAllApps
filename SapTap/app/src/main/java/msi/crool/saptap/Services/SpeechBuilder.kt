package msi.crool.saptap.Services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

object SpeechBuilder {
    private const val URL = "https://arbazkhan-cs-speech2speech-api.hf.space" // Your actual server URL

    // Create an Interceptor to log network requests and responses
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create a custom OkHttpClient with the logging interceptor
    private val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add logging interceptor
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                val newRequest: Request = request.newBuilder().build()
                return chain.proceed(newRequest)
            }
        })

    // Create a Gson instance with lenient parsing
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // Build the Retrofit instance with the custom Gson and OkHttpClient
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL) // Set the base URL to your server URL
        .addConverterFactory(GsonConverterFactory.create(gson)) // Use the custom Gson instance
        .client(okHttp.build()) // Use the custom OkHttpClient
        .build()

    // Create and return the Retrofit service instance
    fun <T> speechBuilder(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}

