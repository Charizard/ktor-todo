package com.todos.domain.service

import com.todos.domain.model.User
import com.todos.domain.model.UserCredentials
import com.todos.domain.repository.TodosRepository
import com.todos.domain.repository.UsersRepository

interface UsersService {
  fun findUserFromCredentials(userCredentials: UserCredentials): Long
}

class UsersServiceImpl(private val usersRepository: UsersRepository) : UsersService {
  override fun findUserFromCredentials(userCredentials: UserCredentials): Long = usersRepository.findUserFromCredentials(userCredentials)
}