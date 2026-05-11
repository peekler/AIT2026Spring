package hu.bme.aut.nfctictactoedemo

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class MainActivity : AppCompatActivity(), GameView.GameStateListener {
    private var mNfcAdapter: NfcAdapter? = null
    private var mNfcPendingIntent: PendingIntent? = null
    private var mWriteTagFilters: Array<IntentFilter?>? = null
    private var inWriteMode = false
    private var gameView: GameView? = null
    private var fieldStateToSave = "1#0,0,0;0,0,0;0,0,0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        mNfcPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(
                this,
                javaClass
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE
        )
        // Intent filters for exchanging over p2p.
        val ndefDetected = IntentFilter(
            NfcAdapter.ACTION_NDEF_DISCOVERED
        )
        try {
            ndefDetected.addDataType("text/plain")
        } catch (e: MalformedMimeTypeException) {
        }

        gameView = findViewById<View?>(R.id.gameView) as GameView
        gameView!!.setGameStateListener(this)
    }

    public override fun onResume() {
        super.onResume()
        if (!inWriteMode) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == getIntent()
                    .getAction()
            ) {
                var msgs: Array<NdefMessage>? = null

                val rawMsgs = getIntent().getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES
                )
                if (rawMsgs != null) {
                    msgs = arrayOfNulls<NdefMessage>(rawMsgs.size) as Array<NdefMessage>?
                    for (i in rawMsgs.indices) {
                        msgs?.set(i, rawMsgs[i] as NdefMessage)
                    }
                }

                if (msgs != null) {
                    for (tmpMsg in msgs) {
                        gameView!!.initField(
                            String(
                                tmpMsg.getRecords()[0]
                                    .getPayload()
                            )
                        )
                    }
                }
            }
        }
    }


    private fun enableTagWriteMode() {
        inWriteMode = true
        val tagDetected = IntentFilter(
            NfcAdapter.ACTION_TAG_DISCOVERED
        )
        mWriteTagFilters = arrayOf<IntentFilter>(tagDetected) as Array<IntentFilter?>?
        mNfcAdapter!!.enableForegroundDispatch(
            this, mNfcPendingIntent,
            mWriteTagFilters, null
        )
    }

    override fun onNewIntent(intent: Intent) {
        // Tag writing mode
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.getAction()) {
            val detectedTag = intent.getParcelableExtra<Tag?>(NfcAdapter.EXTRA_TAG)

            if (fieldStateToSave != "") {
                val record = createCustomRecord(fieldStateToSave)

                val msg = NdefMessage(arrayOf<NdefRecord?>(record))

                if (writeTag(msg, detectedTag)) {
                    Toast.makeText(
                        this, "Success: game state saved!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(this, "Write failed", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        super<AppCompatActivity>.onNewIntent(intent)
    }

    fun createCustomRecord(aData: String): NdefRecord {
        val payload = aData.toByteArray()
        val extRecord = NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            "com.example:externalType".toByteArray(), ByteArray(0), payload
        )
        return extRecord
    }

    fun writeTag(message: NdefMessage, tag: Tag?): Boolean {
        val size = message.toByteArray().size
        try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                if (!ndef.isWritable()) {
                    return false
                }
                if (ndef.getMaxSize() < size) {
                    return false
                }
                ndef.writeNdefMessage(message)
                return true
            } else {
                val format = NdefFormatable.get(tag)
                if (format != null) {
                    try {
                        format.connect()
                        format.format(message)
                        return true
                    } catch (e: IOException) {
                        return false
                    }
                } else {
                    return false
                }
            }
        } catch (e: Exception) {
            return false
        }
    }

    fun onClick(v: View?) {
        enableTagWriteMode()
    }

    fun onClearClick(v: View?) {
        gameEnded()
        Toast.makeText(
            this,
            "Please put the device near to NFC tag to clear the board!",
            Toast.LENGTH_LONG
        ).show()

    }


    override fun playerMoved(aFieldState: String) {
        fieldStateToSave = aFieldState
        enableTagWriteMode()
    }

    override fun gameEnded() {
        fieldStateToSave = "1#0,0,0;0,0,0;0,0,0"
        enableTagWriteMode()
    }
}