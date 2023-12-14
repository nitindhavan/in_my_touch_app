package com.imt.inmytouch
import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Class provides access to do requests.
 * Use [api] value to send one of the requests described in [Api] class.
 * In this representation you're not allowed to change end point on the fly
 */
const val END_POINT = "http://192.168.1.9:2000/" //TODO: put here your server endPoint
object RestClient {

    private const val TIMEOUT = 30L
    const val MAX_REQUEST_COUNT = 3

    val api: Api = provideRetrofit().create(Api::class.java)
    // val apiMN: Api = provideRetrofitMN().create(Api::class.java)

    //val apiWallet: Api = provideWalletRetrofit().create(Api::class.java)

    //Used to send several requests in parallel
    private val dispatcher: Dispatcher
        get() {
            val dispatcher = Dispatcher()
            dispatcher.maxRequests = MAX_REQUEST_COUNT
            return dispatcher
        }

    private val authInterceptor: Interceptor
        get() = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("accept", "application/json")
                .header("token", "TOKEN") //
                .addHeader("Authorization", "Bearer ")  // this line for bearer toekn
                .method(original.method(), original.body())
                .build()
            Log.e("TOKEN:::::", "")
            chain.proceed(request)
        }


    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }


    private fun provideRetrofit(): Retrofit {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .dispatcher(dispatcher)
        if (android.util.Config.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
//

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(CoroutineCallAdapterFactory()) //used only with coroutines
            .baseUrl(END_POINT)
            .client(builder.build())
            .build()
    }

}