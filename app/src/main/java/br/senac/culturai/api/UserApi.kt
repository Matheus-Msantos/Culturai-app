package br.senac.culturai.api

import br.senac.culturai.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/api/login")
    fun login(@Body user: User): Call<User>

    @POST("/api/user")
    fun singUp(@Body user: User): Call<User>
}