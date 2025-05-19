package br.com.fiap.challengebenmequer.component.emojiempre

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.dto.AggregatedEmojiFeedbackDTO
import br.com.fiap.challengebenmequer.viewmodel.CompanyDataViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry as ChartEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

val pieChartColors = listOf(
    Color(0xFF43709E),
    Color(0xFF54AC0F),
    Color(0xFF9A9697),
    Color(0xFFDD712B),
    Color(0xFF474485),
    Color(0xFFE95F4D)
).map { it.toArgb() }

fun getEmojiDrawableRes(emojiName: String?): Int {
    return when (emojiName?.uppercase()) {
        "TRISTE" -> R.drawable.ic_triste_emoji
        "ALEGRE" -> R.drawable.ic_alegre_emoji
        "CANSADO" -> R.drawable.ic_cansado_emoji
        "ANSIOSO" -> R.drawable.ic_ansioso_emoji
        "MEDO" -> R.drawable.ic_medo_emoji
        "RAIVA" -> R.drawable.ic_raiva_emoji
        else -> R.drawable.ic_alegre_emoji
    }
}

@Composable
fun EmojiFeedbackCard(
    modifier: Modifier = Modifier,
    aggregatedEmojiData: List<AggregatedEmojiFeedbackDTO>?,
    isLoading: Boolean,
    apiResponseMessage: String?,
    viewModel: CompanyDataViewModel // Passa o ViewModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top=8.dp), // Adicionado top padding
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) { // Padding interno do Card
            Text(
                text = "Emoji",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFF1E2B4D)
            )

            if (isLoading && aggregatedEmojiData == null) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (aggregatedEmojiData.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(apiResponseMessage ?: "Nenhum dado de emoji disponível para o gráfico.")
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min), // Para alinhar a altura do gráfico e da legenda
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gráfico de Pizza
                    Box(modifier = Modifier.weight(0.6f).aspectRatio(1f)) {
                        AndroidView(
                            factory = { context ->
                                PieChart(context).apply {
                                    description.isEnabled = false
                                    isDrawHoleEnabled = true
                                    setHoleColor(AndroidColor.TRANSPARENT)
                                    setTransparentCircleAlpha(0)
                                    legend.isEnabled = false
                                    setUsePercentValues(true)
                                    setEntryLabelColor(AndroidColor.DKGRAY)
                                    setEntryLabelTextSize(10f)
                                    animateY(1200, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { pieChart ->
                                val entries = ArrayList<PieEntry>()
                                aggregatedEmojiData.forEach { feedbackItem ->
                                    entries.add(PieEntry(feedbackItem.totalCount?.toFloat() ?: 0f, feedbackItem.emoji ?: "N/A"))
                                }

                                if (entries.isNotEmpty()) {
                                    val dataSet = PieDataSet(entries, "")
                                    dataSet.colors = pieChartColors.take(entries.size) + ColorTemplate.MATERIAL_COLORS.toList().drop(pieChartColors.size).take(entries.size - pieChartColors.size)
                                    dataSet.sliceSpace = 2f
                                    dataSet.valueTextColor = AndroidColor.WHITE
                                    dataSet.valueTextSize = 12f
                                    dataSet.valueFormatter = PercentFormatter(pieChart)

                                    dataSet.setDrawValues(true)
                                    pieChart.setDrawEntryLabels(false)

                                    val pieData = PieData(dataSet)
                                    pieChart.data = pieData
                                    pieChart.setUsePercentValues(true)
                                    pieChart.invalidate()

                                    pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                                        override fun onValueSelected(e: ChartEntry?, h: Highlight?) {
                                            if (e is PieEntry) {
                                                val clickedData = aggregatedEmojiData.find { it.emoji == e.label }
                                                viewModel.selectEmojiSlice(clickedData)
                                            }
                                        }
                                        override fun onNothingSelected() {
                                            viewModel.selectEmojiSlice(null)
                                        }
                                    })
                                } else {
                                    pieChart.clear()
                                    pieChart.invalidate()
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    // Legenda Customizada
                    Column(modifier = Modifier.weight(0.4f)) {
                        aggregatedEmojiData.forEachIndexed { index, feedbackItem ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.selectEmojiSlice(feedbackItem) }
                            ) {
                                Image(
                                    painter = painterResource(id = getEmojiDrawableRes(feedbackItem.emoji)),
                                    contentDescription = feedbackItem.emoji,
                                    modifier = Modifier.size(24.dp).clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${feedbackItem.emoji ?: "N/A"} (${feedbackItem.totalCount ?: 0})",
                                    fontSize = 11.sp,
                                    color = Color(0xFF1E2B4D)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EmojiSliceDetailsPopup(
    sliceData: AggregatedEmojiFeedbackDTO,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalhes para Emoji: ${sliceData.emoji ?: "N/A"}") },
        text = {
            Column {
                Text("Total de respostas para este emoji: ${sliceData.totalCount ?: 0}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Distribuição de Níveis:", style = MaterialTheme.typography.titleSmall)
                sliceData.levels?.forEach { levelDetail ->
                    Text(" - Nível ${levelDetail.level ?: "N/A"}: ${levelDetail.count ?: 0} (${String.format("%.1f%%", levelDetail.percentage ?: 0.0)})")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}