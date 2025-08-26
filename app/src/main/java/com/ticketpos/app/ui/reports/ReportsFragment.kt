package com.ticketpos.app.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ticketpos.app.databinding.FragmentReportsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ReportsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
        
        viewModel.loadReportData()
    }

    private fun setupObservers() {
        viewModel.todaySales.observe(viewLifecycleOwner) { total ->
            binding.textViewTodaySales.text = "$${String.format("%.2f", total)}"
        }

        viewModel.monthSales.observe(viewLifecycleOwner) { total ->
            binding.textViewMonthSales.text = "$${String.format("%.2f", total)}"
        }

        viewModel.totalProducts.observe(viewLifecycleOwner) { count ->
            binding.textViewTotalProducts.text = "$count productos"
        }
    }

    private fun setupClickListeners() {
        binding.cardSalesReport.setOnClickListener {
            // Navigate to sales report
        }
        
        binding.cardInventoryReport.setOnClickListener {
            // Navigate to inventory report
        }
        
        binding.cardFinancialReport.setOnClickListener {
            // Navigate to financial report
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
