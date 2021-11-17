package br.senac.culturai.model

data class User(
	val image: String,
	val name: String,
	val id: Int,
	val isAdmin: Int,
	val email: String
)
