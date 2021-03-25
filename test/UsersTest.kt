package com.todos

import com.todos.config.JwtConfig
import io.ktor.http.*
import com.todos.domain.model.User
import com.todos.domain.model.UserCredentials
import com.todos.domain.repository.UsersRepository
import kotlin.test.*
import io.ktor.server.testing.*

class UsersTest {
    private val usersRepository = UsersRepository()
    private val jwtConfig = JwtConfig("https://todos.com/", "jwt-audience")

    @Test
    fun testPostToken() {
        withTestApplication({ module(testing = true) }) {
            usersRepository.deleteAll()

            handleRequest(HttpMethod.Post, "/users/token") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"email": "blah@slkjdg.com", "password": "sample1234"}""")
            }.apply {
                assertEquals(HttpStatusCode.UnprocessableEntity, response.status())
            }

            usersRepository.createUser(User(1, "Yuvaraja", "sample123", "yuv.slm@gmail.com"))

            handleRequest(HttpMethod.Post, "/users/token") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("""{"email": "yuv.slm@gmail.com", "password": "sample123"}""")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertTrue(response.content?.length!! > 0)
            }
        }
    }

    @Test
    fun testGetCurrentUser() {
        withTestApplication({ module(testing = true) }) {
            usersRepository.deleteAll()

            handleRequest(HttpMethod.Get, "/users/current_user").apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
                assertEquals(null, response.content)
            }

            val user = User(1, "Yuvaraja", "sample123", "yuv.slm@gmail.com")
            val userId = usersRepository.createUser(user)
            val token = jwtConfig.generateToken(UserCredentials(user.email, user.password))

            handleRequest(HttpMethod.Get, "/users/current_user") {
                addHeader(HttpHeaders.Authorization, "Bearer $token")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("""{"id":$userId,"name":"Yuvaraja","password":"sample123","email":"yuv.slm@gmail.com"}""", response.content)
            }
        }
    }

}
