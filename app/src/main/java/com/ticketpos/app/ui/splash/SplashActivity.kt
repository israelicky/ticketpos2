package com.ticketpos.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ticketpos.app.R
import com.ticketpos.app.databinding.ActivitySplashBinding
import com.ticketpos.app.ui.auth.LoginActivity
import com.ticketpos.app.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    
    @Inject
    lateinit var authManager: AuthManager
    
    private val handler = Handler(Looper.getMainLooper())
    private val splashDelay = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        checkAuthStatus()
    }

    private fun setupViews() {
        // Setup splash screen animations
        binding.apply {
            // Animate logo
            imageViewLogo.alpha = 0f
            imageViewLogo.animate()
                .alpha(1f)
                .setDuration(1000)
                .start()

            // Animate app name
            textViewAppName.alpha = 0f
            textViewAppName.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(500)
                .start()

            // Animate tagline
            textViewTagline.alpha = 0f
            textViewTagline.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1000)
                .start()

            // Animate progress bar
            progressBar.alpha = 0f
            progressBar.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(1500)
                .start()
        }
    }

    private fun checkAuthStatus() {
        handler.postDelayed({
            if (authManager.isUserLoggedIn()) {
                // User is logged in, go to main activity
                navigateToMain()
            } else {
                // User is not logged in, go to login activity
                navigateToLogin()
            }
        }, splashDelay)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove any pending callbacks
        handler.removeCallbacksAndMessages(null)
    }
}