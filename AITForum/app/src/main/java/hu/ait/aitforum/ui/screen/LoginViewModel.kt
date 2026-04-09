package hu.ait.aitforum.ui.screen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun registerUser(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(
                email,
                password
            )
                .addOnSuccessListener {
                    // ...
                }
                .addOnFailureListener {
                    // ..
                    it.printStackTrace()
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}