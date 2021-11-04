package br.senac.culturai.model

data class Order(
	val id: Int,
	val userId: Int,
	val status: String,
	val ccNumber: String,
	val createdAt: String
)
