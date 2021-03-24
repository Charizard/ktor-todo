package com.todos.domain.model

data class Todo(
  val id: Int,
  val content: String,
  val completed: Boolean
)