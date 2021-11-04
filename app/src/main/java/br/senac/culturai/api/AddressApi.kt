package br.senac.culturai.api

import br.senac.culturai.model.Address
import retrofit2.Call
import retrofit2.http.GET

interface AddressApi {

    @GET("/api/address")
    fun list():Call<List<Address>>
}