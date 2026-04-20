package hu.bme.ait.httpdemo.ui.screen.newsscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.httpdemo.data.NewsResult
import hu.bme.ait.httpdemo.network.NewsAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NewsUiState {
    object Init : NewsUiState
    object Loading : NewsUiState
    data class Success(val news: NewsResult) : NewsUiState
    data class Error(val errorMsg: String) : NewsUiState
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    //val newsAPI: NewsAPI
) : ViewModel() {

    var newsUiState: NewsUiState by mutableStateOf(NewsUiState.Init)

    /*fun getNews() {
        newsUiState = NewsUiState.Loading
        viewModelScope.launch {
            delay(5000)
            newsUiState = try {
                val result = newsAPI.getNews("us",
                    "e670040e6e2c42988d4725bf2413afb4")
                NewsUiState.Success(result)
            } catch (e: Exception) {
                NewsUiState.Error(e.message!!)
            }
        }
    }*/

}