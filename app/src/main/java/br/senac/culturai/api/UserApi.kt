package br.senac.culturai.api

import br.senac.culturai.model.Login
import br.senac.culturai.model.SingUp
import br.senac.culturai.model.Token
import br.senac.culturai.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {


    @POST("/api/login")
    fun login(@Body login: Login): Call<Token>

    @GET("/api/user/show")
    fun user(): Call<User>

    @POST("/api/user")
    fun singUp(@Body user: SingUp): Call<Token>
}