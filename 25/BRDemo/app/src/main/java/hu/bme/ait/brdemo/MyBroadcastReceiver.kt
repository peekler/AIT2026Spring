package hu.bme.ait.brdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "MyBroadcast triggered ${intent.getStringExtra("KEY_DATA")}", Toast.LENGTH_LONG).show()
    }
}