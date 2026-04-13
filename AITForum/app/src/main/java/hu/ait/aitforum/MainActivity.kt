package hu.ait.aitforum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import hu.ait.aitforum.ui.navigation.LoginScreen
import hu.ait.aitforum.ui.navigation.MessagesScreen
import hu.ait.aitforum.ui.navigation.WriteMessageScreen
import hu.ait.aitforum.ui.screen.auth.LoginScreen
import hu.ait.aitforum.ui.screen.messages.MessagesScreen
import hu.ait.aitforum.ui.screen.writemessage.WriteMessageScreen
import hu.ait.aitforum.ui.theme.AITForumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AITForumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNav(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainNav(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(LoginScreen)

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
            entry<LoginScreen> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.add(MessagesScreen)
                    }
                )
            }
            entry<MessagesScreen> {
                MessagesScreen(
                    onWriteMessageClick = {
                        backStack.add(WriteMessageScreen)
                    }
                )
            }
            entry<WriteMessageScreen> {
                WriteMessageScreen()
            }
        }
    )
}