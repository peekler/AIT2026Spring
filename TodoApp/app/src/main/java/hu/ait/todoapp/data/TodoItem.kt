package hu.ait.todoapp.data

data class TodoItem(
    val id: Int = 0,
    val title:String,
    val description:String,
    val createDate:String,
    var priority:TodoPriority,
    var isDone: Boolean
)

enum class TodoPriority {
    NORMAL, HIGH;
}