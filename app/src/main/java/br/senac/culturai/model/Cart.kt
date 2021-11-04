package br.senac.culturai.model

data class Cart(
	val id: Int,
	val userId: Int,
	val productId: Int,
	val quantity: Int

)
