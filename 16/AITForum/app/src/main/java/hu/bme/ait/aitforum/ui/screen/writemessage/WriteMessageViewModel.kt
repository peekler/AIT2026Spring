package hu.bme.ait.aitforum.ui.screen.writemessage

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.ait.aitforum.data.Post
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.UUID


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
        imgUrl: String = ""
    ) {
        writePostUiState = WritePostUiState.LoadingPostUpload

        val myPost = Post(
            postTitle = title,
            postBody = postBody,
            postDate = Date(System.currentTimeMillis()).toString(),
            uid = auth.currentUser!!.uid,
            author = auth.currentUser!!.email!!,
            imgUrl = imgUrl
        )

        val postsCollection = FirebaseFirestore.getInstance().collection(
            COLLECTION_POSTS
        )

        postsCollection.add(myPost)
            .addOnSuccessListener {
                writePostUiState = WritePostUiState.PostUploadSuccess
            }
            .addOnFailureListener {
                writePostUiState = WritePostUiState.ErrorDuringPostUpload(
                    it.message)
            }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    public fun uploadPostImage(
        contentResolver: ContentResolver, imageUri: Uri,
        title: String, postBody: String
    ) {
        viewModelScope.launch {
            writePostUiState = WritePostUiState.LoadingImageUpload

            val source = ImageDecoder.createSource(contentResolver, imageUri)
            val bitmap = ImageDecoder.decodeBitmap(source)

            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageInBytes = baos.toByteArray()

            /*val supabase = createSupabaseClient(
                supabaseUrl = "https://qeelxsehrbdakiapcams.supabase.co",
                supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFlZWx4c2VocmJkYWtpYXBjYW1zIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjI2Mjk5NDEsImV4cCI6MjA3ODIwNTk0MX0.4JjdOT2rP_6HhyCpHciywu-FDa3TlKPvwehcTBG1v_Q",
            ) {
                install(Postgrest)
                install(Storage)
            }*/
            val supabase = createSupabaseClient(
                supabaseUrl = "https://wiyueiusdklanupqajra.supabase.co",
                supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndpeXVlaXVzZGtsYW51cHFhanJhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjMwNDExMDQsImV4cCI6MjA3ODYxNzEwNH0.yBVqPvHxRyimvySui29sJsWh2GVlQixVGgR86J0cORQ",
            ) {
                install(Postgrest)
                install(Storage)
            }

            val bucket = supabase.storage.from("forumimages")
            val fileName = "uploads/${UUID.randomUUID().toString()}.jpg"
            bucket.upload(fileName, imageInBytes)
            val imgUrl = bucket.publicUrl(fileName)
            uploadPost(title, postBody, imgUrl)
        }
    }

}