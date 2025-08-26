package com.ticketpos.app.ui.sales

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticketpos.app.databinding.FragmentSalesBinding
import com.ticketpos.app.ui.sale.SaleActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SalesFragment : Fragment() {

    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SalesViewModel by viewModels()
    private lateinit var salesAdapter: SalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        // Load today's sales
        viewModel.loadTodaySales()
    }

    private fun setupRecyclerView() {
        salesAdapter = SalesAdapter { sale ->
            // Handle sale item click
            viewModel.selectSale(sale)
        }
        
        binding.recyclerViewSales.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = salesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.sales.observe(viewLifecycleOwner) { sales ->
            salesAdapter.submitList(sales)
            binding.textViewEmptyState.visibility = if (sales.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.todayTotal.observe(viewLifecycleOwner) { total ->
            binding.textViewTodayTotal.text = "Total del día: $${String.format("%.2f", total)}"
        }

        viewModel.todaySalesCount.observe(viewLifecycleOwner) { count ->
            binding.textViewTodayCount.text = "$count ventas"
        }
    }

    private fun setupClickListeners() {
        binding.fabNewSale.setOnClickListener {
            startActivity(Intent(requireContext(), SaleActivity::class.java))
        }
        
        binding.buttonViewHistory.setOnClickListener {
            // Navigate to sale history
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTodaySales()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
