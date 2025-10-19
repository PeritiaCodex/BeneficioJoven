package mx.itesm.beneficiojoven.model.data.remote

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Sesión global en memoria.
 *
 * Almacena el `token` JWT emitido por el backend para adjuntarlo en el header
 * `Authorization` en cada petición saliente (véase [RetrofitClient.authInterceptor]).
 */
object Session {
    @Volatile var token: String? = null
    @Volatile var userId: Int? = null
}

/**
 * Cliente Retrofit centralizado de la app.
 *
 * Configura:
 * - `BASE_URL`: debe terminar en `/`.
 * - `Gson` con `disableHtmlEscaping`.
 * - Interceptor de autenticación que agrega `Authorization: Bearer <token>`.
 * - Logging de red a nivel `BODY` con el header de autorización redactado.
 *
 * Expone una instancia perezosa de [BackendApi] en [api].
 */
object RetrofitClient {
    /** URL base del backend (incluye `/beneficioJoven/`). */
    private const val BASE_URL = "https://bj-api.site/beneficioJoven/"

    /** Conversor JSON. */
    private val gson = GsonBuilder().disableHtmlEscaping().create()

    /** Interceptor que agrega el header Authorization si existe token en [Session]. */
    private val authInterceptor = Interceptor { chain ->
        val t = Session.token
        val req = if (t.isNullOrBlank()) chain.request()
        else chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $t")
            .build()
        chain.proceed(req)
    }

    /** Cliente OkHttp con auth y logging. */
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY  // antes: BASIC
                redactHeader("Authorization")
            }
        )
        .build()

    /** Implementación concreta del API remoto. */
    val api: BackendApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(BackendApi::class.java)
    }
}
