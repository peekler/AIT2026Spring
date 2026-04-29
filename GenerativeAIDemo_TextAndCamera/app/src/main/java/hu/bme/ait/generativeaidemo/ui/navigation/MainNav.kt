package hu.bme.ait.generativeaidemo.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

enum class CurrentScreen {
    GenAIText,
    GenAIVision
}

@Serializable
data object GenAITextRoute : NavKey

@Serializable
data object GenAIVisionRoute : NavKey
