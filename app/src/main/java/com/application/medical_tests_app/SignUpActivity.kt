package com.application.medical_tests_app

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    var genders: Array<String> = arrayOf("Male", "Female")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val mySpinner = findViewById<AutoCompleteTextView>(R.id.autoCompleteGender)
        mySpinner.setRawInputType(InputType.TYPE_NULL)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, R.layout.dropdown_item, R.id.gender, genders)
        mySpinner.setAdapter(adapter)

        val nameEditText: EditText = findViewById(R.id.editTextName)
        val emailEditText: EditText = findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val birthdayInput : EditText = findViewById (R.id.editTextBirthday)
        birthdayInput.setOnClickListener { showDatePicker(birthdayInput) }
        val submitButton: CardView = findViewById(R.id.buttonSubmit)
        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            signUpWithEmail(name, email, password)
        }
    }

    private fun signUpWithEmail(name: String, email: String, password: String) {
        if (name == "" || email == "" || password == "") {
            Toast.makeText(this, "Please enter name, email and password", Toast.LENGTH_LONG).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.let {
                        saveUser(name)
                        val mainIntent = Intent(this, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Sign up failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun showDatePicker(birthdayInput: EditText) {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                birthdayInput.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun saveUser(user: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user", user)
        editor.commit()
    }

    fun showDatePicker(view: View) {}

}