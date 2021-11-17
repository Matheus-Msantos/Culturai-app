package br.senac.culturai.model

data class Order(
	val id: Int,
	val userId: Int,
	val status: String,
	val cc_number: String,
	val created_at: String,
	val user: User,
	val order_item: List<OrderItem>
)
