package hu.bme.aut.mymlkitdemo

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.mymlkitdemo.ui.screen.CameraScreen
import hu.bme.aut.mymlkitdemo.ui.screen.CameraScreen2
import hu.bme.aut.mymlkitdemo.ui.theme.MyMLKitDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMLKitDemoTheme {
                var hasCamPermission by remember { mutableStateOf(false) }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { granted -> hasCamPermission = granted }
                )

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }

                if (hasCamPermission) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        CameraScreen(Modifier.padding(innerPadding))
                        //CameraScreen(Modifier.padding(innerPadding))
                    }
                } else {
                    PermissionRationale { permissionLauncher.launch(Manifest.permission.CAMERA) }
                }



            }
        }
    }
}

@Composable
private fun PermissionRationale(onRequest: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Camera permission is required to detect faces.")
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRequest) { Text("Grant permission") }
        }
    }
}