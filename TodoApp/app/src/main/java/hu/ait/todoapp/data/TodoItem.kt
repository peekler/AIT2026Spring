package hu.ait.todoapp.data

import hu.ait.todoapp.R

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

    fun getIcon(): Int {
        return if (this == TodoPriority.NORMAL) R.drawable.normal
            else R.drawable.important
    }
}

// https://drive.google.com/file/d/1fp0VXJ3hka5h-H_ApHGSN8k4Ybz4zXwi/view?usp=sharing