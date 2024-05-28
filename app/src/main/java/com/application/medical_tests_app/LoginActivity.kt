package com.application.medical_tests_app


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val signInButton = findViewById<CardView>(R.id.btnSignIn)

        signInButton.setOnClickListener {
            signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
        }
    }


    private fun signInWithEmailAndPassword(email: String, password: String) {
        if (email == "" || password == "") {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_LONG).show()
            }
        }
    }
}