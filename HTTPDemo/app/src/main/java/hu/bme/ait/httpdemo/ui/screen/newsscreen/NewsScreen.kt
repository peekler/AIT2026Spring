package hu.bme.ait.httpdemo.ui.screen.newsscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.revenuecat.placeholder.PlaceholderDefaults
import com.revenuecat.placeholder.placeholder
import hu.bme.ait.httpdemo.data.Article
import hu.bme.ait.httpdemo.data.NewsResult

@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    Column(modifier = Modifier
        .fillMaxWidth()) {
        Text("News screen")

        Button(onClick = {
            viewModel.getNews()
        }) {
            Text(text = "Refresh")
        }

        when (viewModel.newsUiState) {
            is NewsUiState.Error -> Text("Error during network communication")
            is NewsUiState.Init -> {}
            is NewsUiState.Loading -> ResultScreenPlaceholder()
            is NewsUiState.Success -> ResultScreen(
                (viewModel.newsUiState as NewsUiState.Success).news
            )
        }

    }
}

@Composable
fun ResultScreen(newsResult: NewsResult) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(newsResult.articles!!) {
            NewsCard(it!!,
                isLoading = false)
        }
    }
}


@Composable
fun ResultScreenPlaceholder() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(5) {
            NewsCard(
                null,
                isLoading = true
            )
        }
    }
}



@Composable
fun NewsCard (
    article: Article? = null,
    isLoading: Boolean = false
) {
    val uriHandler = LocalUriHandler.current

    val annotatedString = buildAnnotatedString {
        // We use withLink to handle the click and styling automatically
        withLink(
            LinkAnnotation.Url(
                url = article?.url ?: "",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
                    val url = (it as LinkAnnotation.Url).url
                    if (url.isNotEmpty()) {
                        uriHandler.openUri(url)
                    }
                }
            )
        ) {
            append("Link")
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = article?.author ?: "Author",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.placeholder(
                    enabled = isLoading,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderDefaults.shimmer
                )

            )
            Text(
                text =  article?.publishedAt ?: "Date",
                fontSize = 12.sp,
                modifier = Modifier.placeholder(
                    enabled = isLoading,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderDefaults.shimmer
                )
            )
            Text(
                text = article?.title ?: "Title",
                modifier = Modifier.placeholder(
                    enabled = isLoading,
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderDefaults.shimmer
                )
            )
            if (article?.urlToImage != "") {
                AsyncImage(
                    model = article?.urlToImage,
                    modifier = Modifier
                        .size(200.dp, 100.dp),
                    contentDescription = "selected image"
                )
            }
            Text(
                text = annotatedString
            )
        }
    }
}