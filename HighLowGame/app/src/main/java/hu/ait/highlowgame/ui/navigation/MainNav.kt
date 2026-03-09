package hu.ait.highlowgame.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// list the screens of the app here

@Serializable
data object HomeScreenRoute: NavKey

@Serializable
data class GameScreenRoute(val upperBound: Int): NavKey