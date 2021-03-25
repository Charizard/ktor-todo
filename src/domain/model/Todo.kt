package com.todos.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Todo(
  @JsonProperty("id")
  var id: Int,
  @JsonProperty("content")
  val content: String,
  @JsonProperty("completed")
  val completed: Boolean
)