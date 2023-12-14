package com.imt.inmytouch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.imt.inmytouch.models.MessageResponse
import com.imt.inmytouch.models.RegistrationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmEditText: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signupButton=findViewById(R.id.save_button)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        mobileEditText = findViewById(R.id.phoneEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmEditText = findViewById(R.id.confirmEditText)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val mobile = mobileEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmEditText.text.toString().trim()

            // Check if any of the fields are empty
            if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("All fields are required")
                return@setOnClickListener
            }

            // Check if the email is valid
            if (!isValidEmail(email)) {
                showToast("Invalid email address")
                return@setOnClickListener
            }

            // Check if the mobile number is valid (assuming 10 digits)
            if (mobile.length != 10) {
                showToast("Mobile number should be 10 digits")
                return@setOnClickListener
            }

            // Check if password and confirm password match
            if (password != confirmPassword) {
                showToast("Passwords do not match")
                return@setOnClickListener
            }

            // Call the registerUser method if all checks pass
            registerUser(0, email, mobile, "", name, "", "", password)
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Function to validate email address
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }
    private fun registerUser(
        id: Int,
        email: String,
        mobile: String,
        imei: String,
        name: String,
        gender: String,
        about: String,
        password: String
    ) {
        val progressBar: ProgressBar=findViewById(R.id.progressBar)
        progressBar.visibility= View.VISIBLE
        val registrationRequest = RegistrationRequest(id, email, mobile, imei, name, gender, about, password)

        RestClient.api.registerUser(registrationRequest).enqueue(object :
            Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                // Handle the response here
                progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                    showToast("Registration Successful")
                    finish()
                } else {
                    showToast("Email phone already exists")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                progressBar.visibility= View.INVISIBLE
                showToast(t.message.toString())
            }
        })
    }
}