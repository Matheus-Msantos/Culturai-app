package br.senac.culturai.model

data class Product(
	val id: Int,
	val name: String,
	val price: String,
	val description: String,
	val time: String,
	val date: String,
	val classification: String,
	val image: String,
	val addressId: Int,
	val categoryId: Int,
	val createdAt: String,
	val updatedAt: String
)
