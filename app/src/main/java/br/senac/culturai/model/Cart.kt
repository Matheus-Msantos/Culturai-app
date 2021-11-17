package br.senac.culturai.model

data class Cart(
	val id: Int,
	val user_id: Int,
	val product_id: Int,
	val quantity: Int,
	val created_at: String,
	val updated_at: String,
	val product: Product

)
