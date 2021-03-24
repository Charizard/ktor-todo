package com.todos.routes

import com.todos.domain.model.Todo
import com.todos.domain.repository.TodosRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.todosRoute(todosRepository: TodosRepository) {
  route("/todos") {
    get {
      call.respond(todosRepository.findAll(20))
    }

    get("/{id}") {
      val todoId = call.parameters["id"]?.toInt()
      if (todoId != null) {
        var todo = todosRepository.find(todoId) ?: throw Error("Not Found")
        call.respond(HttpStatusCode.OK, todo)
      }
    }

    post {
      val newTodo = call.receive<Todo>()
      call.respond(HttpStatusCode.Created, todosRepository.createTodo(newTodo))
    }

    delete("/{id}") {
      val todoId = call.parameters["id"]?.toInt()
      if (todoId != null) {
        call.respond(HttpStatusCode.Accepted, todosRepository.deleteTodo(todoId))
      }
    }
  }
}