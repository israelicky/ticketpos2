package com.ticketpos.app.ui.sales

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ticketpos.app.data.entity.Sale
import com.ticketpos.app.databinding.ItemSaleBinding
import java.text.SimpleDateFormat
import java.util.*

class SalesAdapter(
    private val onSaleClick: (Sale) -> Unit
) : ListAdapter<Sale, SalesAdapter.SaleViewHolder>(SaleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val binding = ItemSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SaleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SaleViewHolder(
        private val binding: ItemSaleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sale: Sale) {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            
            binding.apply {
                textViewSaleNumber.text = sale.saleNumber
                textViewTime.text = dateFormat.format(Date(sale.date))
                textViewTotal.text = "$${String.format("%.2f", sale.totalAmount)}"
                textViewStatus.text = when (sale.paymentStatus) {
                    "completed" -> "Pagado"
                    "pending" -> "Pendiente"
                    "cancelled" -> "Cancelado"
                    else -> "Desconocido"
                }
                
                root.setOnClickListener {
                    onSaleClick(sale)
                }
            }
        }
    }

    private class SaleDiffCallback : DiffUtil.ItemCallback<Sale>() {
        override fun areItemsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem == newItem
        }
    }
}
