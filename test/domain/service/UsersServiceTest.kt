package domain.service

import com.todos.domain.model.User
import com.todos.domain.model.UserCredentials
import com.todos.domain.repository.UsersRepository
import com.todos.domain.service.UsersServiceImpl
import io.mockk.every
import io.mockk.mockkClass
import kotlin.test.*

class UsersServiceTest {
  private val mockUsersRepository = mockkClass(UsersRepository::class)
  private val subject = UsersServiceImpl(mockUsersRepository)
  private val allUsers = (1..10).map {
    User(it, "Sample todo $it", "pass123", "sample$it@example.com")
  }

  @Test
  fun `find an user from email`() {
    every { mockUsersRepository.findUserFromEmail(any()) } returns allUsers[0]
    val result = subject.findUserFromEmail("sample1@example.com");
    assertEquals("sample1@example.com", result!!.email)
    assertTrue(result is User)
  }

  @Test
  fun `find an non existing user from email`() {
    every { mockUsersRepository.findUserFromEmail(any()) } returns null
    val result = subject.findUserFromEmail("sample20@example.com");
    assertEquals(null, result)
  }

  @Test
  fun `find an user from Credentials`() {
    every { mockUsersRepository.findUserFromCredentials(any()) } returns 1
    val result = subject.findUserFromCredentials(UserCredentials("sample1@example.com", "sample123"));
    assertEquals(1, result)
  }
}