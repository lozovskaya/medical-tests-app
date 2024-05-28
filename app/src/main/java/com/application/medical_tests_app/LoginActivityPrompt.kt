package com.application.medical_tests_app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivityPrompt : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_prompt)

        auth = FirebaseAuth.getInstance()
        val signInWithEmailButton  = findViewById<CardView>(R.id.btnSignInWithEmail)
        val signInWithGoogleButton = findViewById<CardView>(R.id.btnSignInWithGoogle)
        val signUpButton = findViewById<CardView>(R.id.btnSignUp)

        signInWithEmailButton.setOnClickListener {
            val signInIntent = Intent(this, LoginActivity::class.java)
            startActivity(signInIntent)
        }

        signInWithGoogleButton.setOnClickListener {
            startSignInWithGoogle()
        }

        signUpButton.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun startSignInWithGoogle() {
        Toast.makeText(this, "Google Sign In has not been configured yet.", Toast.LENGTH_SHORT).show()
//        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
//        val signInIntent = AuthUI.getInstance()
//            .createSignInIntentBuilder()
//            .setAvailableProviders(providers)
//            .build()
//        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "Signed in as ${currentUser?.displayName}", Toast.LENGTH_SHORT).show()
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            } else {
                Toast.makeText(this, "Sign-in failed: ${response?.error?.errorCode}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}
