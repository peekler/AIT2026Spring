package hu.ait.aitandroidhello

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hu.ait.aitandroidhello.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // activity_main.xml -> ActivityMainBinding
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        // it loads the main linear layout from the activity_main.xml
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnShowTime.setOnClickListener {
            Toast.makeText(
                this,
                "Button clicked",
                Toast.LENGTH_SHORT).show()

            var myNum = binding.etData.text.toString().toInt()
            binding.tvText.setText("Result: ${myNum*2}")

        }
    }
}