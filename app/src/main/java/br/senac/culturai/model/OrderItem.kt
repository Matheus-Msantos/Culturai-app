package br.senac.culturai.model

data class OrderItem(
	val id: Int,
	val orderId: Int,
	val product_id: Int,
	val quantity: Int,
	val price: String,
	val order: Order
)
