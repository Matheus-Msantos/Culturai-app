package br.senac.culturai.api

import br.senac.culturai.model.Category
import br.senac.culturai.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("/api/product")
    fun list(): Call<List<Product>>

    @GET("/api/product/{id}")
    fun search(@Path("id") id: Int): Call<List<Product>>

    @GET("/api/product/category/{category}")
    fun filter(@Path("category") category: Int): Call <List<Product>>
}