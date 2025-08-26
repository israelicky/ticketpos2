package com.ticketpos.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ticketpos.app.R
import com.ticketpos.app.databinding.ActivityLoginBinding
import com.ticketpos.app.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViews() {
        // Hide action bar for login screen
        supportActionBar?.hide()

        // Setup input fields
        binding.editTextUsername.requestFocus()
        
        // Setup password visibility toggle
        binding.textInputLayoutPassword.setEndIconOnClickListener {
            if (binding.editTextPassword.inputType == android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                binding.editTextPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT
                binding.textInputLayoutPassword.endIconDrawable = getDrawable(R.drawable.ic_visibility_off)
            } else {
                binding.editTextPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.textInputLayoutPassword.endIconDrawable = getDrawable(R.drawable.ic_visibility)
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonLogin.isEnabled = !isLoading
            binding.editTextUsername.isEnabled = !isLoading
            binding.editTextPassword.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                binding.textInputLayoutPassword.error = error
            }
        }

        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                navigateToMain()
            }
        }

        viewModel.failedLoginAttempts.observe(this) { attempts ->
            if (attempts >= 3) {
                showAccountLockedDialog()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            performLogin()
        }

        binding.buttonForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        binding.buttonHelp.setOnClickListener {
            showHelpDialog()
        }

        // Handle enter key press
        binding.editTextPassword.setOnEditorActionListener { _, _, _ ->
            performLogin()
            true
        }
    }

    private fun performLogin() {
        val username = binding.editTextUsername.text.toString().trim()
        val password = binding.editTextPassword.text.toString()

        // Clear previous errors
        binding.textInputLayoutUsername.error = null
        binding.textInputLayoutPassword.error = null

        // Validate input
        if (username.isEmpty()) {
            binding.textInputLayoutUsername.error = "El nombre de usuario es requerido"
            binding.editTextUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.textInputLayoutPassword.error = "La contraseña es requerida"
            binding.editTextPassword.requestFocus()
            return
        }

        // Attempt login
        viewModel.login(username, password)
    }

    private fun showForgotPasswordDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Recuperar Contraseña")
            .setMessage("¿Olvidaste tu contraseña? Contacta al administrador del sistema para restablecerla.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showHelpDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Ayuda")
            .setMessage("""
                Para iniciar sesión:
                
                1. Ingresa tu nombre de usuario
                2. Ingresa tu contraseña
                3. Presiona el botón "Iniciar Sesión"
                
                Si tienes problemas para acceder, contacta al administrador del sistema.
                
                Usuario demo: admin
                Contraseña demo: admin123
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAccountLockedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Cuenta Bloqueada")
            .setMessage("Tu cuenta ha sido bloqueada debido a múltiples intentos fallidos de inicio de sesión. Contacta al administrador para desbloquearla.")
            .setPositiveButton("OK", null)
            .setCancelable(false)
            .show()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onBackPressed() {
        // Prevent going back from login screen
        moveTaskToBack(true)
    }
}