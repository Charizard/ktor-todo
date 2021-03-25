package com.todos.routes

import com.todos.config.JwtConfig
import com.todos.domain.model.User
import com.todos.domain.model.UserCredentials
import com.todos.domain.service.UsersService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.usersRoute() {
  val jwtConfig: JwtConfig by inject()
  val usersService: UsersService by inject()

  route("/users") {
    post("/token") {
      val user = call.receive<UserCredentials>()
      if (usersService.findUserFromCredentials(user) > 0) {
        return@post call.respond(jwtConfig.generateToken(user))
      }
      call.respond(HttpStatusCode.UnprocessableEntity, "User email / password combination is wrong.")
    }
  }
}