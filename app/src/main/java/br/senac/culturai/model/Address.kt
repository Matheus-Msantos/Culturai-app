package br.senac.culturai.model

data class Address(
	val id: Int,
	val street: String,
	val district: String,
	val number: String,
	val city: String,
	val state: String,
	val country: String
)
