package hu.ait.aitforum.ui.screen.writemessage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.aitforum.data.Post
import java.util.Date

sealed interface WritePostUiState {
    object Init : WritePostUiState
    object LoadingPostUpload : WritePostUiState
    object PostUploadSuccess : WritePostUiState
    data class ErrorDuringPostUpload(val error: String?) : WritePostUiState

    object LoadingImageUpload : WritePostUiState
    data class ErrorDuringImageUpload(val error: String?) : WritePostUiState
    object ImageUploadSuccess : WritePostUiState
}

class WriteMessageViewModel : ViewModel() {
    companion object { // static items are inside this
        const val COLLECTION_POSTS = "posts"
    }

    var writePostUiState: WritePostUiState
            by mutableStateOf(WritePostUiState.Init)
    private lateinit var auth: FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun uploadPost(
        title: String,
        postBody: String,
        imageUrl: String = ""
    ) {
        writePostUiState = WritePostUiState.LoadingPostUpload

        val myPost = Post(
            postTitle = title,
            postBody = postBody,
            postDate = Date(System.currentTimeMillis()).toString(),
            uid = auth.currentUser!!.uid,
            author = auth.currentUser!!.email!!,
            imgUrl = imageUrl
        )

        val postCollection = FirebaseFirestore
            .getInstance()
            .collection(COLLECTION_POSTS)

        postCollection.add(myPost)
            .addOnSuccessListener {
                writePostUiState = WritePostUiState.PostUploadSuccess
            }
            .addOnFailureListener {
                writePostUiState = WritePostUiState.ErrorDuringPostUpload(
                    it.localizedMessage
                )
            }
    }

}