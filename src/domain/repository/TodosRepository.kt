package com.todos.domain.repository

import com.todos.domain.model.Todo
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Todos : IntIdTable() {
  val content: Column<String> = text("content")
  val completed: Column<Boolean> = bool("completed")

  fun toDomain(row: ResultRow): Todo {
    return Todo(
      id = row[id].value,
      content = row[content],
      completed = row[completed],
    )
  }
}

class TodosRepository() {
  fun findAll(limit: Int = 10): List<Todo> {
    return transaction {
      Todos.selectAll().limit(limit).map { Todos.toDomain(it) }
    }
  }

  fun createTodo(todo: Todo) {
    return transaction {
      Todos.insert {
        it[content] = todo.content
        it[completed] = todo.completed
      }
    }
  }

  fun deleteTodo(todoId: Int) {
    return transaction {
      Todos.deleteWhere { Todos.id eq todoId }
        .takeIf { it == 0 }
        ?.apply { throw Error("Not Found") }
    }
  }

  fun find(todoId: Int): Todo? {
    return transaction {
      Todos.select { Todos.id eq todoId }
        .map { Todos.toDomain(it) }
        .firstOrNull()
    }
  }
}