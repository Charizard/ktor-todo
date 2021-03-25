package com.todos.domain.service

import com.todos.domain.model.Todo
import com.todos.domain.repository.TodosRepository

interface TodoService {
  fun findAll(limit: Int): List<Todo>
  fun createTodo(todo: Todo): Int
  fun deleteTodo(todoId: Int): Int
  fun find(todoId: Int): Todo?
}

class TodoServiceImpl(private val todosRepository: TodosRepository) : TodoService {
  override fun findAll(limit: Int): List<Todo> = todosRepository.findAll(limit)

  override fun createTodo(todo: Todo): Int = todosRepository.createTodo(todo)

  override fun deleteTodo(todoId: Int): Int = todosRepository.deleteTodo(todoId)

  override fun find(todoId: Int): Todo? = todosRepository.find(todoId)
}