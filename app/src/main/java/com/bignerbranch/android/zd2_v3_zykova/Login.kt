package com.bignerbranch.android.zd2_v3_zykova

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                handleLogin(email, password)
            }
        }
    }

    private fun initViews() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
    }

    private fun handleLogin(email: String, password: String) {
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)

        if (savedEmail == null || savedPassword == null) {
            saveCredentials(email, password)
            Toast.makeText(this, "Аккаунт создан! Добро пожаловать!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            if (email == savedEmail && password == savedPassword) {
                Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Неверный email или пароль!", Toast.LENGTH_SHORT).show()
                emailEditText.setText("")
                passwordEditText.setText("")
            }
        }
    }

    private fun saveCredentials(email: String, password: String) {
        sharedPreferences.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}