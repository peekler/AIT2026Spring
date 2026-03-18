package hu.ait.todoapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// list the screens of the app here

@Serializable
data class SummaryScreenRoute(
    val allTodoNum: Int,
    val importantTodoNum: Int
): NavKey

@Serializable
data object TodoScreenRoute: NavKey