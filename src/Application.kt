package com.todos

import com.todos.config.*
import com.todos.config.configModule
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.routing.*
import com.todos.domain.repository.Todos
import com.todos.routes.todosRoute
import io.ktor.jackson.*
import com.todos.domain.repository.Users
import com.todos.domain.service.UsersService
import com.todos.routes.usersRoute
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger
import java.lang.System.getenv

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val environment: String = if (testing) { "test" } else { "development" }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Koin) {
        slf4jLogger()
        modules(todosModule, usersModule, configModule)
    }


    install(Authentication) {
        val jwtConfig: JwtConfig by inject()
        val usersService: UsersService by inject()

        jwt {
            realm = "com.todos"
            verifier(jwtConfig.makeJwtVerifier())
            validate {
                val email = it.payload.getClaim("email").asString()

                usersService.findUserFromEmail(email)
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
        }
    }

    DBConfig(environment)

    transaction {
        SchemaUtils.create(Todos)
        SchemaUtils.create(Users)
    }

    routing {
        todosRoute()
        usersRoute()
    }
}

