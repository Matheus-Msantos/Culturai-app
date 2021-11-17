package br.senac.culturai.api

import br.senac.culturai.model.Cart
import br.senac.culturai.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApi {
    @GET("/api/cart")
    fun list():Call<List<Cart>>

    @GET("/api/cart/add/{product}")
    fun add(@Path("product") product: Int): Call<Void>

    @GET("/api/cart/remove/{id}")
    fun remove(@Path("id") id: Int): Call<Void>
}