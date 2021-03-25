package com.todos

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.todos.domain.model.Todo
import com.todos.domain.repository.TodosRepository
import io.ktor.jackson.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
    private val todosRepository = TodosRepository()

    @Test
    fun testGetTodos() {
        withTestApplication({ module(testing = true) }) {
            todosRepository.DeleteAll()

            handleRequest(HttpMethod.Get, "/todos").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("[]", response.content)
            }
            val todoId = todosRepository.createTodo(Todo(1, "Sample", false))

            handleRequest(HttpMethod.Get, "/todos").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("""[{"id":$todoId,"content":"Sample","completed":false}]""", response.content)
            }
        }
    }

    @Test
    fun testGetTodo() {
        withTestApplication({ module(testing = true) }) {
            todosRepository.DeleteAll()

            handleRequest(HttpMethod.Get, "/todos/1").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
                assertEquals("Not Found", response.content)
            }
            val todoId = todosRepository.createTodo(Todo(1, "Sample", false))

            handleRequest(HttpMethod.Get, "/todos/$todoId").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("""{"id":$todoId,"content":"Sample","completed":false}""", response.content)
            }
        }
    }

    @Test
    fun testPostTodo() {
        withTestApplication({ module(testing = true) }) {
            todosRepository.DeleteAll()

            handleRequest(HttpMethod.Post, "/todos") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"content": "New sample", "completed": "false"}""")
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                val todo: Todo = ObjectMapper().readValue(
                    response.content,
                    Todo::class.java
                )
                assertEquals(todo.completed, false)
                assertEquals(todo.content, "New sample")
                assertTrue(todo.id is Int)
            }
        }
    }

    @Test
    fun testDeleteTodo() {
        withTestApplication({ module(testing = true) }) {
            todosRepository.DeleteAll()

            handleRequest(HttpMethod.Delete, "/todos/1").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
                assertEquals("Not Found", response.content)
            }
            val todoId = todosRepository.createTodo(Todo(1, "Sample", false))

            handleRequest(HttpMethod.Delete, "/todos/$todoId").apply {
                assertEquals(HttpStatusCode.Accepted, response.status())
                assertEquals(null, response.content)
            }
        }
    }
}
