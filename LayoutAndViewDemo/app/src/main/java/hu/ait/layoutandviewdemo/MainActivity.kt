package hu.ait.layoutandviewdemo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val cityNames = arrayOf(
        "Budapest", "Bukarest",
        "New York",
        "New Delhi",
        "New Orleans")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main_view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            cityNames
        )
        var autoCompleteTextViewCities = findViewById<AutoCompleteTextView>(
            R.id.autoCompleteTextViewCities
        )
        autoCompleteTextViewCities.threshold = 1
        autoCompleteTextViewCities.setAdapter(cityAdapter)
    }
}