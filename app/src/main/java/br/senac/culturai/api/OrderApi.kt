package br.senac.culturai.api

import br.senac.culturai.model.Order
import br.senac.culturai.model.OrderItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApi {
    @GET("/api/order")
    fun list(): Call<List<Order>>

    @POST("/api/order/add")
    fun add(@Body cc_card: Int): Call<List<Order>>

    @GET("/api/order/item")
    fun orderItem(): Call<List<OrderItem>>
}