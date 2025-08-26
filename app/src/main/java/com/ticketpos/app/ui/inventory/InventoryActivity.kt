package com.ticketpos.app.ui.inventory

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.ticketpos.app.R
import com.ticketpos.app.databinding.ActivityInventoryBinding
import com.ticketpos.app.ui.product.AddEditProductActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryBinding
    private val viewModel: InventoryViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViews() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Inventario"

        // Setup product recycler view
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                openProductDetails(product)
            },
            onEditClick = { product ->
                openEditProduct(product)
            },
            onDeleteClick = { product ->
                showDeleteConfirmation(product)
            },
            onStockAdjustment = { product ->
                showStockAdjustmentDialog(product)
            }
        )

        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(this@InventoryActivity)
            adapter = productAdapter
        }

        // Setup search
        binding.editTextSearch.setOnEditorActionListener { _, _, _ ->
            searchProducts()
            true
        }

        // Setup category filter
        binding.spinnerCategory.setOnItemSelectedListener { _, _, position, _ ->
            val category = binding.spinnerCategory.getItemAtPosition(position) as String
            if (category != "Todas las categorías") {
                viewModel.filterByCategory(category)
            } else {
                viewModel.clearCategoryFilter()
            }
        }
    }

    private fun setupObservers() {
        viewModel.products.observe(this) { products ->
            productAdapter.submitList(products)
            updateProductCount(products.size)
        }

        viewModel.categories.observe(this) { categories ->
            // Update category spinner
            val allCategories = listOf("Todas las categorías") + categories
            // Update spinner adapter
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.lowStockProducts.observe(this) { lowStockCount ->
            if (lowStockCount > 0) {
                binding.textViewLowStockWarning.text = "$lowStockCount productos con stock bajo"
                binding.textViewLowStockWarning.visibility = android.view.View.VISIBLE
            } else {
                binding.textViewLowStockWarning.visibility = android.view.View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddProduct.setOnClickListener {
            openAddProduct()
        }

        binding.buttonScanBarcode.setOnClickListener {
            launchBarcodeScanner()
        }

        binding.buttonImport.setOnClickListener {
            showImportDialog()
        }

        binding.buttonExport.setOnClickListener {
            exportInventory()
        }

        binding.buttonReports.setOnClickListener {
            openInventoryReports()
        }
    }

    private fun searchProducts() {
        val query = binding.editTextSearch.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.searchProducts(query)
        } else {
            viewModel.loadAllProducts()
        }
    }

    private fun openProductDetails(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java).apply {
            putExtra("product_id", product.id)
        }
        startActivity(intent)
    }

    private fun openAddProduct() {
        val intent = Intent(this, AddEditProductActivity::class.java)
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE)
    }

    private fun openEditProduct(product: Product) {
        val intent = Intent(this, AddEditProductActivity::class.java).apply {
            putExtra("product_id", product.id)
        }
        startActivityForResult(intent, EDIT_PRODUCT_REQUEST_CODE)
    }

    private fun showDeleteConfirmation(product: Product) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar Producto")
            .setMessage("¿Estás seguro de que quieres eliminar '${product.name}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteProduct(product)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showStockAdjustmentDialog(product: Product) {
        StockAdjustmentDialog(
            this,
            product,
            onStockAdjusted = { adjustmentType, quantity, reason ->
                viewModel.adjustStock(product.id, adjustmentType, quantity, reason)
            }
        ).show()
    }

    private fun launchBarcodeScanner() {
        // Launch barcode scanner activity
        Snackbar.make(binding.root, "Escáner de código de barras", Snackbar.LENGTH_SHORT).show()
    }

    private fun showImportDialog() {
        ImportDialog(
            this,
            onImportSelected = { importType ->
                when (importType) {
                    "csv" -> importFromCSV()
                    "excel" -> importFromExcel()
                    "manual" -> importManual()
                }
            }
        ).show()
    }

    private fun importFromCSV() {
        // Launch file picker for CSV import
        Snackbar.make(binding.root, "Importar desde CSV", Snackbar.LENGTH_SHORT).show()
    }

    private fun importFromExcel() {
        // Launch file picker for Excel import
        Snackbar.make(binding.root, "Importar desde Excel", Snackbar.LENGTH_SHORT).show()
    }

    private fun importManual() {
        // Show manual import dialog
        Snackbar.make(binding.root, "Importación manual", Snackbar.LENGTH_SHORT).show()
    }

    private fun exportInventory() {
        viewModel.exportInventory()
        Snackbar.make(binding.root, "Exportando inventario...", Snackbar.LENGTH_SHORT).show()
    }

    private fun openInventoryReports() {
        val intent = Intent(this, InventoryReportsActivity::class.java)
        startActivity(intent)
    }

    private fun updateProductCount(count: Int) {
        binding.textViewProductCount.text = "$count productos"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ADD_PRODUCT_REQUEST_CODE -> {
                    Snackbar.make(binding.root, "Producto agregado exitosamente", Snackbar.LENGTH_SHORT).show()
                    viewModel.loadAllProducts()
                }
                EDIT_PRODUCT_REQUEST_CODE -> {
                    Snackbar.make(binding.root, "Producto actualizado exitosamente", Snackbar.LENGTH_SHORT).show()
                    viewModel.loadAllProducts()
                }
            }
        }
    }

    companion object {
        private const val ADD_PRODUCT_REQUEST_CODE = 2001
        private const val EDIT_PRODUCT_REQUEST_CODE = 2002
    }
}