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
import com.todos.domain.model.User
import com.todos.domain.repository.Users
import com.todos.domain.service.TodoService
import com.todos.domain.service.UsersService
import com.todos.routes.usersRoute
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
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

    val dataSource: DataSource = if (testing) {
        DBConfig("jdbc:postgresql://localhost/todos_test", "charizard", "").getDataSource()
    } else {
        DBConfig("jdbc:postgresql://localhost/todos", "charizard", "").getDataSource()
    }
    Database.connect(dataSource)


    transaction {
        SchemaUtils.create(Todos)
        SchemaUtils.create(Users)
    }

    routing {
        todosRoute()
        usersRoute()
    }
}

