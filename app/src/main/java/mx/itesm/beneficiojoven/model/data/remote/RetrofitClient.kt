package mx.itesm.beneficiojoven.model.data.remote

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Session {
    @Volatile var token: String? = null
}

object RetrofitClient {
    private const val BASE_URL = "https://bj-api.site/beneficioJoven/"

    private val gson = GsonBuilder().disableHtmlEscaping().create()

    private val authInterceptor = Interceptor { chain ->
        val t = Session.token
        val req = if (t.isNullOrBlank()) chain.request()
        else chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $t")
            .build()
        chain.proceed(req)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY  // antes: BASIC
                redactHeader("Authorization")
            }
        )
        .build()


    val api: BackendApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(BackendApi::class.java)
    }
}
