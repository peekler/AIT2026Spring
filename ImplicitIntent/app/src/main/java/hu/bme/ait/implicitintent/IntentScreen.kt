package hu.bme.ait.implicitintent

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun IntentScreen(modifier: Modifier) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Button(onClick = {
            //intentSearch(context)
            //intentCall(context)
            //intentSend(context)
            //intentWaze(context)
            //intentStreetMaps(context)
            intentSpotify(context)
            //playNotificationTone(context)
        }) {
            Text(text = "Intent start")
        }
    }
}

private fun intentSpotify(context: Context) {
    // Spotify's internal URI for Liked Songs (Favorites)
    val spotifyUri = Uri.parse("spotify:collection:tracks:play")
    val intent = Intent(Intent.ACTION_VIEW, spotifyUri)

    // Explicitly targeting the Spotify package if you want to ensure it doesn't open in a browser
    intent.setPackage("com.spotify.music")

    // Check if Spotify is installed before starting
    //if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    /*} else {
        // Fallback: Open Play Store if Spotify is missing
        val playStoreIntent = Intent(Intent.ACTION_VIEW,
            Uri.parse("market://details?id=com.spotify.music"))
        context.startActivity(playStoreIntent)
    }*/
}

private fun playNotificationTone(context: Context) {
    val uriNotif = RingtoneManager.getDefaultUri(
        RingtoneManager.TYPE_RINGTONE
    )
    val r = RingtoneManager.getRingtone(
        context, uriNotif
    )
    r.play()
}



fun intentSearch(context: Context) {
    val intent = Intent(Intent.ACTION_WEB_SEARCH)
    intent.putExtra(SearchManager.QUERY,"Balaton")
    context.startActivity(intent)
}

private fun intentCall(context: Context) {
    val intentCall = Intent(Intent.ACTION_DIAL,
        Uri.parse("tel:+36208225581")
    )
    context.startActivity(intentCall)
}

private fun intentSend(context: Context) {
    val intentSend = Intent(Intent.ACTION_SEND)
    intentSend.type = "text/plain"
    intentSend.`package` = "com.facebook.katana"
    intentSend.putExtra(Intent.EXTRA_TEXT, "Share Demo")
    context.startActivity(intentSend)
    //startActivity(Intent.createChooser(intentSend, "Select share app"));
}


private fun intentWaze(context: Context) {
    //String wazeUri = "waze://?favorite=Home&navigate=yes";
    //val wazeUri = "waze://?ll=40.761043, -73.980545&navigate=yes"
    val wazeUri = "waze://?q=MOL&navigate=yes"
    val intentTest = Intent(Intent.ACTION_VIEW)
    intentTest.data = Uri.parse(wazeUri)
    context.startActivity(intentTest)
}

private fun intentStreetMaps(context: Context) {
    val gmmIntentUri = Uri.parse(
        "google.streetview:cbll=29.9774614,31.1329645&cbp=0,30,0,0,-15")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}