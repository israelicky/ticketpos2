package com.ticketpos.app.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticketpos.app.databinding.FragmentInventoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: InventoryViewModel by viewModels()
    private lateinit var inventoryAdapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        viewModel.loadProducts()
    }

    private fun setupRecyclerView() {
        inventoryAdapter = InventoryAdapter { product ->
            viewModel.selectProduct(product)
        }
        
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = inventoryAdapter
        }
    }

    private fun setupObservers() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            inventoryAdapter.submitList(products)
            binding.textViewEmptyState.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.lowStockCount.observe(viewLifecycleOwner) { count ->
            binding.textViewLowStock.text = "$count productos con stock bajo"
        }
    }

    private fun setupClickListeners() {
        binding.fabAddProduct.setOnClickListener {
            // Navigate to add product
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
