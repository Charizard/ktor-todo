package domain.service

import com.todos.domain.model.Todo
import com.todos.domain.repository.TodosRepository
import com.todos.domain.service.TodoServiceImpl
import io.mockk.every
import io.mockk.mockkClass
import kotlin.test.*

class TodoServiceTest {
  private val mockTodoRepository = mockkClass(TodosRepository::class)
  private val subject = TodoServiceImpl(mockTodoRepository)
  private val allTodos = (1..10).map {
    Todo(it, "Sample todo $it", false)
  }

  @Test
  fun `find all Todos with limit 10`() {
    every { mockTodoRepository.findAll(10) } returns allTodos
    val result = subject.findAll(10);
    assertEquals(10, result.count())
    assertTrue(result is List<Todo>)
  }

  @Test
  fun `find all Todos with limit 20`() {
    every { mockTodoRepository.findAll(20) } returns allTodos
    val result = subject.findAll(20);
    assertEquals(10, result.count())
    assertTrue(result is List<Todo>)
  }

  @Test
  fun `find all Todos with limit 5`() {
    every { mockTodoRepository.findAll(5) } returns allTodos.subList(0, 5)
    val result = subject.findAll(5);
    assertEquals(5, result.count())
    assertTrue(result is List<Todo>)
  }

  @Test
  fun `create a Todo`() {
    every { mockTodoRepository.createTodo(any()) } returns 1
    val result = subject.createTodo(Todo(id = 1, content = "sample", completed = false));
    assertEquals(1, result)
    assertTrue(result is Int)
  }

  @Test
  fun `delete a Todo`() {
    every { mockTodoRepository.deleteTodo(any()) } returns 1
    val result = subject.deleteTodo(1);
    assertEquals(1, result)
    assertTrue(result is Int)
  }

  @Test
  fun `find a Todo`() {
    every { mockTodoRepository.find(any()) } returns allTodos[0]
    val result = subject.find(1);
    assertEquals(allTodos[0], result)
    assertTrue(result is Todo)
  }
}