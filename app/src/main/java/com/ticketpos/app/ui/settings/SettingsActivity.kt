package com.ticketpos.app.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.ticketpos.app.R
import com.ticketpos.app.databinding.ActivitySettingsBinding
import com.ticketpos.app.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
        setupClickListeners()

        // Load settings fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }
    }

    private fun setupViews() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Configuración"
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.successMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonBackup.setOnClickListener {
            performBackup()
        }

        binding.buttonRestore.setOnClickListener {
            performRestore()
        }

        binding.buttonExport.setOnClickListener {
            exportSettings()
        }

        binding.buttonImport.setOnClickListener {
            importSettings()
        }

        binding.buttonReset.setOnClickListener {
            showResetConfirmation()
        }

        binding.buttonLogout.setOnClickListener {
            logout()
        }
    }

    private fun performBackup() {
        viewModel.performBackup()
        Snackbar.make(binding.root, "Iniciando respaldo...", Snackbar.LENGTH_SHORT).show()
    }

    private fun performRestore() {
        // Launch file picker for restore
        Snackbar.make(binding.root, "Selecciona archivo de respaldo", Snackbar.LENGTH_SHORT).show()
    }

    private fun exportSettings() {
        viewModel.exportSettings()
        Snackbar.make(binding.root, "Exportando configuración...", Snackbar.LENGTH_SHORT).show()
    }

    private fun importSettings() {
        // Launch file picker for import
        Snackbar.make(binding.root, "Selecciona archivo de configuración", Snackbar.LENGTH_SHORT).show()
    }

    private fun showResetConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Restablecer Configuración")
            .setMessage("¿Estás seguro de que quieres restablecer toda la configuración? Esta acción no se puede deshacer.")
            .setPositiveButton("Restablecer") { _, _ ->
                resetSettings()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun resetSettings() {
        viewModel.resetSettings()
        Snackbar.make(binding.root, "Configuración restablecida", Snackbar.LENGTH_SHORT).show()
    }

    private fun logout() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.logout()
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            
            setupPreferences()
        }

        private fun setupPreferences() {
            // Setup preference change listeners
            findPreference("company_name")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle company name change
                true
            }

            findPreference("tax_rate")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle tax rate change
                true
            }

            findPreference("currency")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle currency change
                true
            }

            findPreference("language")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle language change
                true
            }

            findPreference("receipt_header")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle receipt header change
                true
            }

            findPreference("receipt_footer")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle receipt footer change
                true
            }

            findPreference("auto_backup")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle auto backup change
                true
            }

            findPreference("backup_frequency")?.setOnPreferenceChangeListener { _, newValue ->
                // Handle backup frequency change
                true
            }

            findPreference("printer_settings")?.setOnPreferenceClickListener {
                // Open printer settings
                true
            }

            findPreference("payment_settings")?.setOnPreferenceClickListener {
                // Open payment settings
                true
            }

            findPreference("user_management")?.setOnPreferenceClickListener {
                // Open user management
                true
            }

            findPreference("database_settings")?.setOnPreferenceClickListener {
                // Open database settings
                true
            }

            findPreference("about")?.setOnPreferenceClickListener {
                // Show about dialog
                showAboutDialog()
                true
            }
        }

        private fun showAboutDialog() {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Acerca de TicketPOS")
                .setMessage("""
                    TicketPOS v1.0
                    
                    Sistema de Punto de Venta
                    Desarrollado para pequeñas y medianas empresas
                    
                    Características:
                    • Gestión de ventas
                    • Control de inventario
                    • Reportes detallados
                    • Múltiples métodos de pago
                    • Impresión de recibos
                    • Respaldo automático
                    
                    © 2024 TicketPOS. Todos los derechos reservados.
                """.trimIndent())
                .setPositiveButton("OK", null)
                .show()
        }
    }
}