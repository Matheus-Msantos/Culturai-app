package br.senac.culturai.api

import br.senac.culturai.model.Category
import retrofit2.Call
import retrofit2.http.GET


interface CategoryApi {

    @GET("/api/category")
    fun list(): Call<List<Category>>
}