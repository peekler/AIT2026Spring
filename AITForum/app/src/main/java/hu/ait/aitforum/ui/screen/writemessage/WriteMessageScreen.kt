package hu.ait.aitforum.ui.screen.writemessage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WriteMessageScreen(
    viewModel: WriteMessageViewModel = viewModel()
) {
    var postTitle by remember { mutableStateOf("") }
    var postBody by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        OutlinedTextField(
            value = postTitle,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Post title") },
            onValueChange = {
                postTitle = it
            }
        )
        OutlinedTextField(
            value = postBody,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Post body") },
            onValueChange = {
                postBody = it
            }
        )

        Button(
            onClick = {
                viewModel.uploadPost(
                    postTitle, postBody)
            }
        ) {
            Text("Send")
        }

        when (viewModel.writePostUiState) {
            is WritePostUiState.ErrorDuringImageUpload -> {}
            is WritePostUiState.ErrorDuringPostUpload -> {
                Text(text =
                    "${(viewModel.writePostUiState as 
                            WritePostUiState.ErrorDuringPostUpload)
                        .error}")
            }
            is WritePostUiState.ImageUploadSuccess -> {}
            is WritePostUiState.Init -> {}
            is WritePostUiState.LoadingImageUpload -> {}
            is WritePostUiState.LoadingPostUpload ->
                CircularProgressIndicator()
            is WritePostUiState.PostUploadSuccess ->
                Text("Post uploaded")
        }
    }
}