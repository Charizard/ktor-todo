package com.todos.config

import com.todos.domain.repository.TodosRepository
import com.todos.domain.service.TodoService
import com.todos.domain.service.TodoServiceImpl
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.koin.experimental.builder.single


val todosModule = module {
  singleBy<TodoService, TodoServiceImpl>()
  single<TodosRepository>()
}