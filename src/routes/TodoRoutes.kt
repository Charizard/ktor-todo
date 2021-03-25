package com.todos.routes

import com.todos.domain.model.Todo
import com.todos.domain.repository.TodosRepository
import com.todos.domain.service.TodoService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.todosRoute() {
  val todosService: TodoService by inject()

  route("/todos") {
    get {
      call.respond(todosService.findAll(20))
    }

    get("/{id}") {
      val todoId = call.parameters["id"]?.toInt()
      if (todoId != null) {
        var todo = todosService.find(todoId) ?: return@get call.respond(HttpStatusCode.NotFound, "Not Found")
        call.respond(HttpStatusCode.OK, todo)
      }
    }

    post {
      val newTodo = call.receive<Todo>()
      newTodo.id = todosService.createTodo(newTodo)
      call.respond(HttpStatusCode.Created, newTodo)
    }

    delete("/{id}") {
      val todoId = call.parameters["id"]?.toInt()
      if (todoId != null) {
        if (todosService.deleteTodo(todoId) != 0) {
          call.respond(HttpStatusCode.Accepted)
        } else {
          return@delete call.respond(HttpStatusCode.NotFound, "Not Found")
        }
      }
    }
  }
}