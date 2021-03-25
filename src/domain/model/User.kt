package com.todos.domain.model

import io.ktor.auth.*

data class User(val id: Int, val name: String, val password: String, val email: String): Principal

class UserCredentials(
  val email: String,
  val password: String
)