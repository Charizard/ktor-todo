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
  fun findAll(limit: Int = 20): List<Todo> {
    return transaction {
      Todos.selectAll().limit(limit).map { Todos.toDomain(it) }
    }
  }

  fun createTodo(todo: Todo): Int {
    return transaction {
      Todos.insertAndGetId {
        it[content] = todo.content
        it[completed] = todo.completed
      }.value
    }
  }

  fun deleteTodo(todoId: Int): Int {
    return transaction {
      Todos.deleteWhere { Todos.id eq todoId }
    }
  }

  fun find(todoId: Int): Todo? {
    return transaction {
      Todos.select { Todos.id eq todoId }
        .map { Todos.toDomain(it) }
        .firstOrNull()
    }
  }

  fun deleteAll() {
    return transaction {
      Todos.deleteAll()
    }
  }
}