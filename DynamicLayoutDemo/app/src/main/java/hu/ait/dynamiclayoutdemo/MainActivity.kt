package hu.ait.dynamiclayoutdemo

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hu.ait.dynamiclayoutdemo.databinding.ActivityMainBinding
import hu.ait.dynamiclayoutdemo.databinding.TodoLayoutBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding //actvity_main.xml

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSave.setOnClickListener {
            // todo_layout.xml
            var todoLayoutBinding = TodoLayoutBinding.inflate(
                layoutInflater)

            todoLayoutBinding.tvTodoTitle.text =
                binding.etTodo.text

            todoLayoutBinding.btnDel.setOnClickListener {
                binding.layoutTodos.removeView(
                    todoLayoutBinding.root)
            }

            binding.layoutTodos.addView(todoLayoutBinding.root)
        }

    }
}