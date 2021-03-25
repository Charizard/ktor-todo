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
        var todo = todosRepository.find(todoId) ?: return@get call.respond(HttpStatusCode.NotFound, "Not Found")
        call.respond(HttpStatusCode.OK, todo)
      }
    }

    post {
      val newTodo = call.receive<Todo>()
      newTodo.id = todosRepository.createTodo(newTodo)
      call.respond(HttpStatusCode.Created, newTodo)
    }

    delete("/{id}") {
      val todoId = call.parameters["id"]?.toInt()
      if (todoId != null) {
        if (todosRepository.deleteTodo(todoId) != 0) {
          call.respond(HttpStatusCode.Accepted)
        } else {
          return@delete call.respond(HttpStatusCode.NotFound, "Not Found")
        }
      }
    }
  }
}