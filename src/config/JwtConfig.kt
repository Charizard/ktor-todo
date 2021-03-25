package com.todos.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.todos.domain.model.UserCredentials
import java.util.*

class JwtConfig(private val jwtIssuer: String, private val jwtAudience: String) {
  private val validityInMs by lazy { 36_000_00 * 24 } // 1 day
  private val algorithm = Algorithm.HMAC256("secret")

  fun makeJwtVerifier(): JWTVerifier = JWT
    .require(algorithm)
    .withAudience(jwtAudience)
    .withIssuer(jwtIssuer)
    .build()

  fun generateToken(user: UserCredentials): String = JWT.create()
    .withSubject("Authentication")
    .withIssuer(jwtIssuer)
    .withAudience(jwtAudience)
    .withClaim("email", user.email)
    .withExpiresAt(getExpiration())
    .sign(algorithm)

  private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}