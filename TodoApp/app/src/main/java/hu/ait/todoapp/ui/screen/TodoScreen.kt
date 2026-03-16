package hu.ait.todoapp.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.todoapp.data.TodoItem
import hu.ait.todoapp.data.TodoPriority
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoListViewModel = viewModel()
) {
    var todoText by remember { mutableStateOf("") }
    var showTodoDialog by rememberSaveable {
        mutableStateOf(false) }

    var todoToEdit: TodoItem? by rememberSaveable { mutableStateOf(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {Text("AIT Todo")},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor =
                    MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(
                    onClick = {
                        showTodoDialog = true
                    }
                ) {
                    Icon(Icons.Filled.AddCircle, null)
                }
            }
        )

        if (showTodoDialog) {
            TodoDialog(
                viewModel,
                todoToEdit,
                onCancel = {
                    showTodoDialog = false
                    todoToEdit = null // turn off edit mode when dialog was cancelled
                }
            )
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
                    },
                    onCheckClicked = {
                        checked -> viewModel.changeTodoState(
                            todo, checked
                        )
                    },
                    onTodoEdit = { selectedTodo ->
                        todoToEdit = selectedTodo
                        showTodoDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun TodoCard(
    todoItem: TodoItem,
    onDeleteClicked: (TodoItem) -> Unit = {},
    onCheckClicked: (Boolean) -> Unit = {},
    onTodoEdit: (TodoItem) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

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
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy, // controls "elasticity"
                        stiffness = Spring.StiffnessMedium // lower = slower, more bounce
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id = todoItem.priority.getIcon()),
                    contentDescription = "Priority",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )

                Text(
                    todoItem.title,
                    modifier = Modifier.fillMaxWidth(0.2f),
                    style = if (todoItem.isDone) {
                        TextStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    } else {
                        TextStyle(
                            textDecoration = TextDecoration.None
                        )
                    }
                )
                Spacer(modifier = Modifier.fillMaxSize(0.35f))
                Checkbox(
                    checked = todoItem.isDone,
                    onCheckedChange = {
                        onCheckClicked(it)
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
                        onTodoEdit(todoItem)
                    },
                    tint = Color.Blue
                )

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }

            }

            if (expanded) {
                Text(
                    todoItem.description)
                Text(
                    todoItem.createDate)
            }
        }
    }
}



@Composable
fun TodoDialog(
    viewModel: TodoListViewModel,
    todoToEdit: TodoItem? = null,
    onCancel: () -> Unit
) {
    var todoTitle by remember {
        mutableStateOf(
            todoToEdit?.title ?: ""
        )
    }
    var todoDesc by remember {
        mutableStateOf(
            todoToEdit?.description ?: ""
        )
    }
    var important by remember {
        mutableStateOf(
            if (todoToEdit != null) {
                todoToEdit.priority == TodoPriority.HIGH
            } else {
                false
            }
        )
    }

    Dialog(onDismissRequest = {
        onCancel()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    if (todoToEdit == null) "New Todo" else "Edit Todo",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Todo title") },
                    value = "$todoTitle",
                    onValueChange = { todoTitle = it })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Todo description") },
                    value = "$todoDesc",
                    onValueChange = { todoDesc = it })
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = important,
                        onCheckedChange = { important = it })
                    Text("Important")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (todoToEdit == null) {
                            viewModel.addTodoList(
                                TodoItem(
                                    title = todoTitle,
                                    description = todoDesc,
                                    createDate = Date(System.currentTimeMillis()).toString(),
                                    priority = if (important) TodoPriority.HIGH else TodoPriority.NORMAL,
                                    isDone = false
                                )
                            )
                        } else {
                            val editedTodo = todoToEdit.copy(
                                title = todoTitle,
                                description = todoDesc,
                                priority = if (important) TodoPriority.HIGH else TodoPriority.NORMAL
                            )
                            viewModel.updateTodo(
                                todoToEdit,
                                editedTodo
                            )
                        }

                        onCancel()
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
