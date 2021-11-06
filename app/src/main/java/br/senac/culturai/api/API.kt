package br.senac.culturai.api

import br.senac.culturai.EventListFragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class API(val context: EventListFragment){
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
}