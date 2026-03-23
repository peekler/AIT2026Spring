package hu.ait.todoapp.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.todoapp.data.TodoDAO
import hu.ait.todoapp.data.TodoItem
import hu.ait.todoapp.data.TodoPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(val todoDAO: TodoDAO) : ViewModel() {

    fun getAllToDoList(): Flow<List<TodoItem>> {
        return todoDAO.getAllTodos()
    }

    suspend fun getAllTodoNum() : Int {
        return todoDAO.getTodosNum()
    }

    suspend fun getImportantTodoNum() : Int {
        return todoDAO.getImportantTodosNum()
    }

    fun addTodoList(todoItem: TodoItem) {
        viewModelScope.launch() { // we launch a coroutine here
            todoDAO.insert(todoItem)
        }
    }

    fun removeTodoItem(todoItem: TodoItem) {
        viewModelScope.launch() { // we launch a coroutine here
            todoDAO.delete(todoItem)
        }
    }

    fun changeTodoState(todoItem: TodoItem, value: Boolean) {
        val newTodo = todoItem.copy()
        newTodo.isDone = value
        viewModelScope.launch() { // we launch a coroutine here
            todoDAO.update(newTodo)
        }
    }

    fun updateTodo(editedTodo: TodoItem) {
        viewModelScope.launch() { // we launch a coroutine here
            todoDAO.update(editedTodo)
        }
    }

    fun removeAllItem() {
        viewModelScope.launch() { // we launch a coroutine here
            todoDAO.deleteAllTodos()
        }
    }
}
