package com.ticketpos.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ticketpos.app.databinding.FragmentSettingsBinding
import com.ticketpos.app.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
        
        viewModel.loadUserInfo()
    }

    private fun setupObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            binding.textViewUserName.text = user?.fullName ?: "Usuario"
            binding.textViewUserRole.text = user?.role ?: "ADMIN"
        }
    }

    private fun setupClickListeners() {
        binding.cardUserManagement.setOnClickListener {
            // Navigate to user management
        }
        
        binding.cardCompanySettings.setOnClickListener {
            // Navigate to company settings
        }
        
        binding.cardPrinterSettings.setOnClickListener {
            // Navigate to printer settings
        }
        
        binding.cardBackup.setOnClickListener {
            // Navigate to backup/restore
        }
        
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
