package br.senac.culturai.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val FILE_LOGIN = "login"

class API(private val context: Context){
    private val baseUrl = "http://10.0.2.2:8000"
    private val timeOut = 30L

    private val retrofit: Retrofit get() {
        val okHttp = OkHttpClient.Builder()
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val product: ProductApi get() {
        return retrofit.create(ProductApi::class.java)
    }

    val category: CategoryApi get() {
        return retrofit.create(CategoryApi::class.java)
    }

    val filter: ProductApi get() {
        return retrofit.create(ProductApi::class.java)
    }

    val login: UserApi get() {
        return retrofit.create(UserApi::class.java)
    }

    val singUp: UserApi get(){
        return retrofit.create(UserApi::class.java)
    }

    private val retrofitSecurity: Retrofit get() {
        val authenticator = AuthenticatorToken(context)
        val okHttp = OkHttpClient.Builder()
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(authenticator)
            .authenticator(authenticator)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
    }

    val account: UserApi get() {
        return retrofitSecurity.create(UserApi::class.java)
    }

    val order: OrderApi get() {
        return retrofitSecurity.create(OrderApi::class.java)
    }

    val cart: CartApi get() {
        return retrofitSecurity.create(CartApi::class.java)
    }

}