package hu.ait.aitandroidhello

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
            Log.d("TAG_DEMO", "My fancy button was clicked")



            Toast.makeText(
                this,
                getString(R.string.text_button_clicked),
                Toast.LENGTH_SHORT).show()

            try {
                if (binding.etData.text.isEmpty()) {
                    binding.etData.setError("This field can not be empty")
                } else {
                    var myNum = binding.etData.text.toString().toInt()

                    //<string name="text_result">Result: %1$d %2$s</string>

                    binding.tvText.setText(
                        getString(R.string.text_result,5,"Peter")
                    )
                    revealCard()
                }
            } catch (e: Exception) {
                binding.etData.setError("Error in this field: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun revealCard() {
        val x = binding.cardView.getRight()
        val y = binding.cardView.getBottom()

        val startRadius = 0
        val endRadius = Math.hypot(binding.cardView.getWidth().toDouble(),
            binding.cardView.getHeight().toDouble()).toInt()

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.cardView,
            x,
            y,
            startRadius.toFloat(),
            endRadius.toFloat()
        )

        binding.cardView.setVisibility(View.VISIBLE)
        anim.setDuration(5000) // millisec
        anim.start()
    }



}