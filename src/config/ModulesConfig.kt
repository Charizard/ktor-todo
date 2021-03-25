package com.todos.config

import com.todos.domain.repository.TodosRepository
import com.todos.domain.repository.UsersRepository
import com.todos.domain.service.TodoService
import com.todos.domain.service.TodoServiceImpl
import com.todos.domain.service.UsersService
import com.todos.domain.service.UsersServiceImpl
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.koin.experimental.builder.single

val todosModule = module {
  singleBy<TodoService, TodoServiceImpl>()
  single<TodosRepository>()
}

val usersModule = module {
  singleBy<UsersService, UsersServiceImpl>()
  single<UsersRepository>()
}

val configModule = module {
  single { JwtConfig(getProperty("jwt.domain", "https://todos.com/"), getProperty("jwt.audience", "jwt-audience")) }
}