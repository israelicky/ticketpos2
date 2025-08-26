package com.ticketpos.app.ui.sale

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ticketpos.app.databinding.ItemCartBinding
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class CartAdapter(
    private val onQuantityChanged: (CartItem, Double) -> Unit,
    private val onItemRemoved: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.apply {
                textViewProductName.text = item.productName
                textViewProductSku.text = item.productSku
                textViewUnitPrice.text = formatCurrency(item.unitPrice)
                textViewTotalPrice.text = formatCurrency(item.totalPrice)
                
                // Setup quantity stepper
                textViewQuantity.text = item.quantity.toString()
                
                buttonIncrease.setOnClickListener {
                    val newQuantity = item.quantity + 1
                    onQuantityChanged(item, newQuantity)
                }
                
                buttonDecrease.setOnClickListener {
                    val newQuantity = item.quantity - 1
                    if (newQuantity > 0) {
                        onQuantityChanged(item, newQuantity)
                    }
                }
                
                buttonRemove.setOnClickListener {
                    onItemRemoved(item)
                }
                
                // Show tax info if applicable
                if (item.taxRate > BigDecimal.ZERO) {
                    textViewTaxInfo.text = "IVA ${item.taxRate}%"
                    textViewTaxInfo.visibility = android.view.View.VISIBLE
                } else {
                    textViewTaxInfo.visibility = android.view.View.GONE
                }
                
                // Show unit info
                textViewUnit.text = item.unit
            }
        }

        private fun formatCurrency(amount: BigDecimal): String {
            return NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(amount)
        }
    }

    private class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}