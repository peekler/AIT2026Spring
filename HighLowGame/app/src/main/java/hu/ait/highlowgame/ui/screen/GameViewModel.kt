package hu.ait.highlowgame.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.ait.highlowgame.ui.navigation.GameScreenRoute

class GameViewModel(
    gameScreenRoute: GameScreenRoute
) : ViewModel() {

    var generatedNumber = mutableStateOf(0)
    var counter = mutableStateOf(0)
    var upperBound = 3

    init {
        upperBound = gameScreenRoute.upperBound
        generatedRandomNumber(upperBound)
    }

    private fun generatedRandomNumber(upperBound: Int) {
        //generatedNumber.value = Random.nextInt(upperBound)
        generatedNumber.value = (0..upperBound).random()
    }

    fun increaseCounter() {
        counter.value++
    }

    fun reset() {
        counter.value = 0
        generatedRandomNumber(upperBound)
    }


    class Factory(
        private val key: GameScreenRoute,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GameViewModel(key) as T
        }
    }

}