package hu.bme.ait.aitforum

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
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import hu.bme.ait.aitforum.ui.navigation.LoginScreen
import hu.bme.ait.aitforum.ui.navigation.MessagesScreen
import hu.bme.ait.aitforum.ui.navigation.WriteMessageScreen
import hu.bme.ait.aitforum.ui.screen.login.LoginScreen
import hu.bme.ait.aitforum.ui.screen.messages.MessagesScreen
import hu.bme.ait.aitforum.ui.screen.writemessage.WriteMessageScreen
import hu.bme.ait.aitforum.ui.theme.AITForumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AITForumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun NavGraph(modifier: Modifier) {
    val backStack = rememberNavBackStack(LoginScreen)

    NavDisplay(
        //modifier = modifier,
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider  = entryProvider {
            entry<LoginScreen> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.add(MessagesScreen)
                    }
                )
            }
            entry<MessagesScreen> {
                MessagesScreen(
                    onNewMessageClick = {
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
