package com.todos.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

class DBConfig(val environment: String) {
  private lateinit var dataSource: DataSource
  private lateinit var jdbcUrl: String
  private lateinit var username: String
  private lateinit var password: String

  init {
    var config = dbConfig()
    if (config != null) {
      jdbcUrl = config.getString("url")
      username = config.getString("username")
      password = config.getString("password")

      connect()
    }
  }

  private fun connect() {
    dataSource = HikariConfig().let { config ->
      config.jdbcUrl = jdbcUrl
      config.username = username
      config.password = password
      HikariDataSource(config)
    }
    Database.connect(dataSource)
  }

  private fun dbConfig(): Config? {
    return ConfigFactory.load().getConfig("$environment.dbConfig.dataSource")
  }
}