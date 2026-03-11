package hu.ait.todoapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.todoapp.data.TodoItem
import hu.ait.todoapp.data.TodoPriority
import java.util.Date

@Composable
fun TodoScreen(
    viewModel: TodoListViewModel = viewModel()
) {
    var todoText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Enter todo here")},
            value = todoText,
            onValueChange = {
                todoText = it
            }
        )
        Button(onClick = {
            viewModel.addTodoList(
                TodoItem(
                    id = 0,
                    title = todoText,
                    description = "lorem ipsum...",
                    createDate = Date(System.currentTimeMillis()).toString(),
                    isDone = false,
                    priority = TodoPriority.NORMAL
                )
            )
        }) {
            Text("Add")
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(viewModel.getAllToDoList()) { todo ->
                TodoCard(todo,
                    onDeleteClicked = {
                        todoToDelete -> viewModel.removeTodoItem(
                            todoToDelete)
                    }
                )
            }
        }
    }
}

@Composable
fun TodoCard(
    todoItem: TodoItem,
    onDeleteClicked: (TodoItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            //modifier = Modifier.padding(20.dp).animateContentSize()
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    todoItem.title,
                    modifier = Modifier.fillMaxWidth(0.2f),
                )
                Spacer(modifier = Modifier.fillMaxSize(0.35f))
                Checkbox(
                    checked = todoItem.isDone,
                    onCheckedChange = {
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        onDeleteClicked(todoItem)
                    },
                    tint = Color.Red
                )
                Icon(
                    imageVector = Icons.Filled.Build,
                    contentDescription = "Edit",
                    modifier = Modifier.clickable {
                    },
                    tint = Color.Blue
                )
            }
        }
    }
}