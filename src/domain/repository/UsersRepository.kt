package com.todos.domain.repository

import com.todos.domain.model.User
import com.todos.domain.model.UserCredentials
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : IntIdTable() {
  val email: Column<String> = varchar("email", 50).uniqueIndex()
  val password: Column<String> = varchar("password", 50)
  val name: Column<String> = varchar("name", 50)

  fun toDomain(row: ResultRow): User {
    return User(
      id = row[id].value,
      email = row[email],
      password = row[password],
      name = row[name],
    )
  }
}

class UsersRepository() {
  fun findUserFromCredentials(userCredentials: UserCredentials): Long {
    return transaction {
      Users.select { Users.email eq userCredentials.email }
        .count()
    }
  }
}