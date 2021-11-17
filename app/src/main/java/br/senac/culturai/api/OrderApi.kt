package br.senac.culturai.api

import br.senac.culturai.model.Order
import br.senac.culturai.model.OrderItem
import br.senac.culturai.model.Payment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApi {
    @GET("/api/order")
    fun list(): Call<List<Order>>

    @POST("/api/order/add")
    fun add(@Body cc_card: Payment): Call<Order>

    @GET("/api/order/item/{orderItem}")
    fun orderItem(@Path("orderItem") orderItem: Int): Call<List<OrderItem>>
}