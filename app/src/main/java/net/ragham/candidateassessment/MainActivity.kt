package net.ragham.candidateassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.ragham.candidateassessment.ui.invoice.InvoiceEditorScreen
import net.ragham.candidateassessment.ui.invoice.InvoiceItemAddScreen
import net.ragham.candidateassessment.ui.theme.CandidateAssessmentTheme
import net.ragham.candidateassessment.viewmodel.InvoiceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CandidateAssessmentTheme {
                // ایجاد و مدیریت NavController
                val navController = rememberNavController()
                // ایجاد ViewModel برای استفاده در کل نمودار ناوبری
                val invoiceViewModel: InvoiceViewModel = viewModel()

                NavHost(navController = navController, startDestination = "invoice_editor") {
                    
                    // صفحه اصلی: نمایش لیست آیتم‌ها
                    composable("invoice_editor") {
                        InvoiceEditorScreen(
                            viewModel = invoiceViewModel,
                            onAddItem = {
                                navController.navigate("add_item")
                            },
                            onEditItem = { itemId ->
                                navController.navigate("edit_item/$itemId")
                            }
                        )
                    }

                    // صفحه افزودن آیتم جدید
                    composable("add_item") {
                        InvoiceItemAddScreen(
                            products = invoiceViewModel.products,
                            initialItem = null,
                            onSave = { newItem ->
                                invoiceViewModel.addOrUpdateItem(newItem)
                                navController.popBackStack()
                            },
                            onCancel = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // صفحه ویرایش آیتم موجود
                    composable(
                        route = "edit_item/{itemId}",
                        arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getString("itemId")
                        val itemToEdit = itemId?.let { invoiceViewModel.getItem(it) }

                        if (itemToEdit != null) {
                            InvoiceItemAddScreen(
                                products = invoiceViewModel.products,
                                initialItem = itemToEdit,
                                onSave = { updatedItem ->
                                    invoiceViewModel.addOrUpdateItem(updatedItem)
                                    navController.popBackStack()
                                },
                                onCancel = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
