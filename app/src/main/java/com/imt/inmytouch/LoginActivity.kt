package com.imt.inmytouch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.JsonObject
import com.imt.inmytouch.models.LoginModel
import com.imt.inmytouch.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var signupButton: Button
    lateinit var loginButton: Button

    lateinit var emailET: EditText
    lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton=findViewById(R.id.loginButton)
        emailET=findViewById(R.id.emailEditText)
        password=findViewById(R.id.passwordEditText)
        loginButton.setOnClickListener {
            val email = emailET.text.toString().trim()
            val password = password.text.toString()

            // Validate email
            if (email.isEmpty()) {
                Toast.makeText(this@LoginActivity,"Email or phone should not be empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate phone number
            if (password.isEmpty()) {
                Toast.makeText(this@LoginActivity,"Email or phone should not be empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // If all validations pass, call the login function
            login(email,  password)
        }
        signupButton=findViewById(R.id.createAccountButton)
        signupButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
        }
    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginModel(email, password)
        val progressBar: ProgressBar=findViewById(R.id.progressBar)
        progressBar.visibility= View.VISIBLE
        RestClient.api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Handle the response here
                progressBar.visibility= View.INVISIBLE
                if (response.isSuccessful) {
                    val loginResponse = response.body()!!
                    val editor=getSharedPreferences("sharedp", MODE_PRIVATE).edit()
                    editor.putInt("uid",loginResponse.id)
                    editor.apply()
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    Toast.makeText(this@LoginActivity,"Login successful",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity,"Invalid credential",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar.visibility= View.INVISIBLE
                Toast.makeText(this@LoginActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        })
    }

}