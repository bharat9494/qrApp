package com.skqr.skqr.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.skqr.skqr.R
import com.skqr.skqr.data.models.LoginModel
import com.skqr.skqr.databinding.ActivityLoginBinding
import com.skqr.skqr.misc.userId
import com.skqr.skqr.viewModel.LoginViewModel
import com.skqr.skqr.viewModel.LoginViewModelFactory

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.appOrange, theme)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        viewModelFactory = LoginViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.loginResponse.observe(this) { loginResponse ->
            if (loginResponse.isSuccessful) {
                val data = loginResponse.body()
                if(data?.status == "1") {
                    Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()

                    with(sharedPreferences.edit()) {
                        putString(userId, data.id)
                        apply()
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, data?.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, loginResponse.message(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        if(view == binding.btnLogin) {

            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(username.isEmpty()) {
                Toast.makeText(this, "Username can not be empty", Toast.LENGTH_SHORT).show()
                binding.etUsername.requestFocus()
                return
            }

            if(password.isEmpty()) {
                Toast.makeText(this, "Password can not be empty", Toast.LENGTH_SHORT).show()
                binding.etPassword.requestFocus()
                return
            }

            viewModel.validateUser(
                LoginModel(username, password)
            )
        }

    }
}