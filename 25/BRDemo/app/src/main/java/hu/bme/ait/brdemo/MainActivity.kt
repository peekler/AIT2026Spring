package hu.bme.ait.brdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.ait.brdemo.service.BroadcastService
import hu.bme.ait.brdemo.ui.theme.BRDemoTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            BRDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        airPlaneReceiver = AirPlaneReceiver()
        screenReceiver = ScreenReceiver()
        bootReceiver = BootReceiver()
        myBroadcastReceiver = MyBroadcastReceiver()

        registerReceiver(
            airPlaneReceiver,
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        )

        registerReceiver(
            screenReceiver,
            IntentFilter(Intent.ACTION_SCREEN_ON)
        )
        registerReceiver(
            screenReceiver,
            IntentFilter(Intent.ACTION_SCREEN_OFF)
        )

        registerReceiver(
            bootReceiver,
            IntentFilter(Intent.ACTION_BOOT_COMPLETED)
        )

        /*registerReceiver(
            myBroadcastReceiver,
            IntentFilter("hu.bme.aut.NOTIFY")
        )*/

        requestNeededPermission()
    }

    private lateinit var airPlaneReceiver: AirPlaneReceiver
    private lateinit var screenReceiver: ScreenReceiver
    private lateinit var bootReceiver: BootReceiver
    private lateinit var myBroadcastReceiver: MyBroadcastReceiver

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,
                Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,
                Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.PROCESS_OUTGOING_CALLS,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                101)
        } else {
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "All perm granted", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(this@MainActivity,
                        "Permissions NOT granted", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onDestroy() {
                unregisterReceiver(
                        airPlaneReceiver
                )

                unregisterReceiver(
                        screenReceiver
                )

                unregisterReceiver(
                        bootReceiver
                )

                /*unregisterReceiver(
                        myBroadcastReceiver
                )*/

        super.onDestroy()
    }





}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var localContext = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Button(
            onClick = {
                localContext.startForegroundService(Intent(localContext, BroadcastService::class.java))
            }
        ) {
            Text("Start service")
        }
        Button(
            onClick = {
                localContext.stopService(Intent(localContext, BroadcastService::class.java))
            }
        ) {
            Text("Stop service")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BRDemoTheme {
        Greeting("Android")
    }
}