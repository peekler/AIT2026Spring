package hu.ait.aitforum.ui.screen.messages

import androidx.lifecycle.ViewModel
import hu.ait.aitforum.data.PostWithId

sealed interface MessagesUIState {
    object Init : MessagesUIState
    object Loading : MessagesUIState
    data class Success(
        val postList: List<PostWithId>) : MessagesUIState
    data class Error(val error: String?) : MessagesUIState
}

class MessagesViewModel : ViewModel() {
}