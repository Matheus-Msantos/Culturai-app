package br.senac.culturai.api

import br.senac.culturai.model.Category
import br.senac.culturai.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path


interface CategoryApi {

    @GET("/api/category")
    fun list(): Call<List<Category>>
}