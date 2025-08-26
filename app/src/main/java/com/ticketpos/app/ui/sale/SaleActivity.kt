package com.ticketpos.app.ui.sale

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ticketpos.app.R
import com.ticketpos.app.databinding.ActivitySaleBinding
import com.ticketpos.app.ui.payment.PaymentActivity
import com.ticketpos.app.ui.receipt.ReceiptActivity
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class SaleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaleBinding
    private val viewModel: SaleViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViews() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Nueva Venta"

        // Setup cart recycler view
        cartAdapter = CartAdapter(
            onQuantityChanged = { item, newQuantity ->
                viewModel.updateItemQuantity(item.id, newQuantity)
            },
            onItemRemoved = { item ->
                viewModel.removeItemFromCart(item.id)
            }
        )

        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(this@SaleActivity)
            adapter = cartAdapter
        }

        // Setup product search
        binding.editTextSearch.setOnEditorActionListener { _, _, _ ->
            searchProduct()
            true
        }
    }

    private fun setupObservers() {
        viewModel.cartItems.observe(this) { items ->
            cartAdapter.submitList(items)
            updateCartSummary()
        }

        viewModel.searchResults.observe(this) { products ->
            // Handle search results
            if (products.isNotEmpty()) {
                showProductSelectionDialog(products)
            } else {
                Snackbar.make(binding.root, "No se encontraron productos", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.cartTotal.observe(this) { total ->
            binding.textViewTotal.text = formatCurrency(total)
        }

        viewModel.cartSubtotal.observe(this) { subtotal ->
            binding.textViewSubtotal.text = formatCurrency(subtotal)
        }

        viewModel.cartTax.observe(this) { tax ->
            binding.textViewTax.text = formatCurrency(tax)
        }

        viewModel.cartDiscount.observe(this) { discount ->
            binding.textViewDiscount.text = formatCurrency(discount)
        }
    }

    private fun setupClickListeners() {
        binding.buttonScanBarcode.setOnClickListener {
            // Launch barcode scanner
            launchBarcodeScanner()
        }

        binding.buttonAddProduct.setOnClickListener {
            searchProduct()
        }

        binding.buttonDiscount.setOnClickListener {
            showDiscountDialog()
        }

        binding.buttonPayment.setOnClickListener {
            if (viewModel.cartItems.value?.isNotEmpty() == true) {
                proceedToPayment()
            } else {
                Snackbar.make(binding.root, "El carrito está vacío", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.buttonVoid.setOnClickListener {
            showVoidConfirmation()
        }

        binding.buttonHold.setOnClickListener {
            holdSale()
        }
    }

    private fun searchProduct() {
        val query = binding.editTextSearch.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.searchProducts(query)
        }
    }

    private fun showProductSelectionDialog(products: List<Product>) {
        // Show dialog to select product and quantity
        ProductSelectionDialog(
            this,
            products,
            onProductSelected = { product, quantity ->
                viewModel.addProductToCart(product, quantity)
                binding.editTextSearch.text?.clear()
            }
        ).show()
    }

    private fun launchBarcodeScanner() {
        // Launch barcode scanner activity
        // This would integrate with a barcode scanning library
        Snackbar.make(binding.root, "Escáner de código de barras", Snackbar.LENGTH_SHORT).show()
    }

    private fun showDiscountDialog() {
        DiscountDialog(
            this,
            onDiscountApplied = { percentage ->
                viewModel.applyDiscount(percentage)
            }
        ).show()
    }

    private fun proceedToPayment() {
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("sale_id", viewModel.currentSaleId)
            putExtra("total_amount", viewModel.cartTotal.value?.toString())
        }
        startActivityForResult(intent, PAYMENT_REQUEST_CODE)
    }

    private fun showVoidConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Anular Venta")
            .setMessage("¿Estás seguro de que quieres anular esta venta?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.voidSale()
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun holdSale() {
        viewModel.holdSale()
        Snackbar.make(binding.root, "Venta en espera", Snackbar.LENGTH_SHORT).show()
        finish()
    }

    private fun updateCartSummary() {
        val itemCount = viewModel.cartItems.value?.size ?: 0
        binding.textViewItemCount.text = "$itemCount artículos"
    }

    private fun formatCurrency(amount: BigDecimal): String {
        return NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(amount)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Payment successful, show receipt
            val receiptIntent = Intent(this, ReceiptActivity::class.java).apply {
                putExtra("sale_id", viewModel.currentSaleId)
            }
            startActivity(receiptIntent)
            finish()
        }
    }

    companion object {
        private const val PAYMENT_REQUEST_CODE = 1001
    }
}