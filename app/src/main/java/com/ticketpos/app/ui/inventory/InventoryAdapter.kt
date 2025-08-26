package com.ticketpos.app.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ticketpos.app.data.entity.Product
import com.ticketpos.app.databinding.ItemInventoryBinding

class InventoryAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, InventoryAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemInventoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val binding: ItemInventoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                textViewProductName.text = product.name
                textViewCategory.text = product.category
                textViewPrice.text = "$${String.format("%.2f", product.price)}"
                textViewStock.text = "Stock: ${product.stock}"
                
                // Color code low stock
                if (product.stock <= 10) {
                    textViewStock.setTextColor(
                        binding.root.context.getColor(android.R.color.holo_red_dark)
                    )
                } else {
                    textViewStock.setTextColor(
                        binding.root.context.getColor(android.R.color.darker_gray)
                    )
                }
                
                root.setOnClickListener {
                    onProductClick(product)
                }
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
