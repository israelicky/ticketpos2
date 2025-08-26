package com.ticketpos.app.ui.reports

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ticketpos.app.R
import com.ticketpos.app.databinding.ActivityReportsBinding
import com.ticketpos.app.ui.reports.detail.SalesReportDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportsBinding
    private val viewModel: ReportsViewModel by viewModels()
    private lateinit var salesReportAdapter: SalesReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupViews() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Reportes"

        // Setup sales report recycler view
        salesReportAdapter = SalesReportAdapter(
            onReportClick = { report ->
                openSalesReportDetail(report)
            },
            onExportClick = { report ->
                exportReport(report)
            }
        )

        binding.recyclerViewSalesReports.apply {
            layoutManager = LinearLayoutManager(this@ReportsActivity)
            adapter = salesReportAdapter
        }

        // Setup date range picker
        setupDateRangePicker()
    }

    private fun setupDateRangePicker() {
        val today = Calendar.getInstance()
        val startOfMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        binding.editTextStartDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(startOfMonth.time))
        binding.editTextEndDate.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(today.time))

        binding.buttonApplyDateRange.setOnClickListener {
            applyDateRange()
        }
    }

    private fun setupObservers() {
        viewModel.salesReports.observe(this) { reports ->
            salesReportAdapter.submitList(reports)
        }

        viewModel.inventoryReports.observe(this) { reports ->
            // Handle inventory reports
        }

        viewModel.summaryData.observe(this) { summary ->
            updateSummaryDisplay(summary)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonGenerateReport.setOnClickListener {
            generateReport()
        }

        binding.buttonExportAll.setOnClickListener {
            exportAllReports()
        }

        binding.buttonPrint.setOnClickListener {
            printReports()
        }

        binding.buttonEmail.setOnClickListener {
            emailReports()
        }

        binding.buttonShare.setOnClickListener {
            shareReports()
        }

        binding.buttonSchedule.setOnClickListener {
            scheduleReports()
        }
    }

    private fun applyDateRange() {
        try {
            val startDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .parse(binding.editTextStartDate.text.toString())
            val endDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .parse(binding.editTextEndDate.text.toString())

            if (startDate != null && endDate != null) {
                viewModel.setDateRange(startDate, endDate)
                generateReport()
            } else {
                Snackbar.make(binding.root, "Fechas inválidas", Snackbar.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Error en formato de fecha", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun generateReport() {
        viewModel.generateReports()
        Snackbar.make(binding.root, "Generando reportes...", Snackbar.LENGTH_SHORT).show()
    }

    private fun openSalesReportDetail(report: SalesReport) {
        val intent = Intent(this, SalesReportDetailActivity::class.java).apply {
            putExtra("report_id", report.id)
            putExtra("start_date", report.startDate.time)
            putExtra("end_date", report.endDate.time)
        }
        startActivity(intent)
    }

    private fun exportReport(report: SalesReport) {
        viewModel.exportReport(report)
        Snackbar.make(binding.root, "Exportando reporte...", Snackbar.LENGTH_SHORT).show()
    }

    private fun exportAllReports() {
        viewModel.exportAllReports()
        Snackbar.make(binding.root, "Exportando todos los reportes...", Snackbar.LENGTH_SHORT).show()
    }

    private fun printReports() {
        viewModel.printReports()
        Snackbar.make(binding.root, "Preparando para impresión...", Snackbar.LENGTH_SHORT).show()
    }

    private fun emailReports() {
        viewModel.emailReports()
        Snackbar.make(binding.root, "Enviando reportes por email...", Snackbar.LENGTH_SHORT).show()
    }

    private fun shareReports() {
        viewModel.shareReports()
        Snackbar.make(binding.root, "Compartiendo reportes...", Snackbar.LENGTH_SHORT).show()
    }

    private fun scheduleReports() {
        showScheduleDialog()
    }

    private fun showScheduleDialog() {
        ScheduleReportDialog(
            this,
            onScheduleCreated = { schedule ->
                viewModel.scheduleReport(schedule)
                Snackbar.make(binding.root, "Reporte programado exitosamente", Snackbar.LENGTH_SHORT).show()
            }
        ).show()
    }

    private fun updateSummaryDisplay(summary: ReportSummary) {
        binding.apply {
            textViewTotalSales.text = formatCurrency(summary.totalSales)
            textViewTotalTransactions.text = summary.totalTransactions.toString()
            textViewAverageTicket.text = formatCurrency(summary.averageTicket)
            textViewTopProduct.text = summary.topProduct ?: "N/A"
            textViewTopCategory.text = summary.topCategory ?: "N/A"
            textViewTotalCustomers.text = summary.totalCustomers.toString()
            textViewNewCustomers.text = summary.newCustomers.toString()
        }
    }

    private fun formatCurrency(amount: Double): String {
        return String.format("$%.2f", amount)
    }

    override fun onResume() {
        super.onResume()
        // Refresh reports when returning to this activity
        viewModel.refreshReports()
    }
}