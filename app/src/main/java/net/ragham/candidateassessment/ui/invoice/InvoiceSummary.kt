package net.ragham.candidateassessment.ui.invoice

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.ragham.candidateassessment.ui.util.NumberUtils
import java.math.BigDecimal

/**
 * کامپوننت نمایش خلاصه فاکتور.
 * Composable for displaying invoice summary totals.
 *
 * @param totalBeforeDiscount جمع کل قبل از تخفیف
 * @param totalDiscount جمع کل تخفیف
 * @param finalTotal مبلغ قابل پرداخت نهایی
 */
@Composable
fun InvoiceSummary(
    totalBeforeDiscount: BigDecimal,
    totalDiscount: BigDecimal,
    finalTotal: BigDecimal
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "خلاصه فاکتور",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(label = "جمع کل:", value = NumberUtils.formatCurrency(totalBeforeDiscount))
            SummaryRow(label = "تخفیف:", value = NumberUtils.formatCurrency(totalDiscount))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow(
                label = "مبلغ نهایی:",
                value = NumberUtils.formatCurrency(finalTotal),
                isBold = true
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}
