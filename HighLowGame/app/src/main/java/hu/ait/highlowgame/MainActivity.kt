package hu.ait.highlowgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import hu.ait.highlowgame.ui.navigation.GameScreenRoute
import hu.ait.highlowgame.ui.navigation.HomeScreenRoute
import hu.ait.highlowgame.ui.screen.GameScreen
import hu.ait.highlowgame.ui.screen.HomeScreen
import hu.ait.highlowgame.ui.theme.HighLowGameTheme
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import hu.ait.highlowgame.ui.navigation.AboutScreenRoute
import hu.ait.highlowgame.ui.navigation.HelpScreenRoute
import hu.ait.highlowgame.ui.screen.AboutScreen
import hu.ait.highlowgame.ui.screen.GameViewModel
import hu.ait.highlowgame.ui.screen.HelpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HighLowGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    // this is why the navigation starts with the HomeScreen
    val backStack = rememberNavBackStack(HomeScreenRoute)

    // NavDisplay can actually display the current screen from those listed in the entryProvider
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<HomeScreenRoute> {
                HomeScreen(
                    onStartClicked = {
                        backStack.add(GameScreenRoute(100))
                    },
                    onHelpClicked = {
                        backStack.add(HelpScreenRoute("Peter", 12))
                    },
                    onAboutClicked = {
                        backStack.add(AboutScreenRoute)
                    }
                )
            }
            entry<HelpScreenRoute> {
                // it is the HelpScreenRoute
                // instance which has the name property..
                HelpScreen(it.userName, it.num)
            }
            entry<AboutScreenRoute> {
                AboutScreen()
            }
            entry<GameScreenRoute> {
                // here the "it" will be this object: GameScreenRoute(100)
                // the it.upperBound is the "100" value
                GameScreen(
                    viewModel = viewModel(
                        factory = GameViewModel.Factory(it)
                    )
                )
            }
        },
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith slideOutHorizontally(
                targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(
                targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(
                targetOffsetX = { it })
        }
    )
}