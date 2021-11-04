package br.senac.culturai.model

data class User(
	val id: Int,
	val name: String,
	val email: String,
	val password: String,
	val image: String,
	val isAdmin: Int
)
