package hu.ait.todoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.todoapp.R

@Entity(tableName = "todotable")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "createDate") val createDate:String,
    @ColumnInfo(name = "priority") var priority:TodoPriority,
    @ColumnInfo(name = "isDone") var isDone: Boolean
)

enum class TodoPriority {
    NORMAL, HIGH;

    fun getIcon(): Int {
        return if (this == TodoPriority.NORMAL) R.drawable.normal
            else R.drawable.important
    }
}

// https://drive.google.com/file/d/1fp0VXJ3hka5h-H_ApHGSN8k4Ybz4zXwi/view?usp=sharing