package hu.ait.todoapp.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hu.ait.todoapp.data.TodoItem
import hu.ait.todoapp.data.TodoPriority

class TodoListViewModel : ViewModel() {
    private var _todoList =
        mutableStateListOf<TodoItem>(
            TodoItem(0,"Todo1", "Desc",
                "11.03.2026.",
                TodoPriority.NORMAL,
                false)
        )

    fun getAllToDoList(): List<TodoItem> {
        return _todoList
    }

    fun addTodoList(todoItem: TodoItem) {
        _todoList.add(todoItem)
    }

    fun removeTodoItem(todoItem: TodoItem) {
        _todoList.remove(todoItem)
    }

    fun changeTodoState(todoItem: TodoItem, value: Boolean) {
        val index = _todoList.indexOf(todoItem)

        val newTodo = todoItem.copy(
            title = todoItem.title,
            description = todoItem.description,
            createDate = todoItem.createDate,
            priority = todoItem.priority,
            isDone = value
        )

        _todoList[index] = newTodo
    }
}
