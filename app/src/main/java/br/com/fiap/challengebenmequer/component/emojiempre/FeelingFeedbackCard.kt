package br.com.fiap.challengebenmequer.component.emojiempre

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.fiap.challengebenmequer.dto.AggregatedFeelingFeedbackDTO
import br.com.fiap.challengebenmequer.viewmodel.CompanyDataViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import android.graphics.Color as AndroidColor
import com.github.mikephil.charting.data.Entry as ChartEntry

val feelingPieChartColors = listOf(
    Color(0xFFADD8E6),
    Color(0xFF87CEEB),
    Color(0xFF4682B4),
    Color(0xFF55648F),
    Color(0xFF90EE90),
    Color(0xFF32CD32)
).map { it.toArgb() }

// Função para obter um placeholder de imagem para sentimentos ou cores para legenda
// Diferente de emojis, sentimentos podem não ter ícones dedicados facilmente.
// Podemos usar a cor da legenda como um indicador visual.
fun getFeelingLegendColor(index: Int): Color {
    return Color(feelingPieChartColors.getOrElse(index) { ColorTemplate.getHoloBlue() })
}

@Composable
fun FeelingFeedbackCard(
    modifier: Modifier = Modifier,
    aggregatedFeelingData: List<AggregatedFeelingFeedbackDTO>?,
    isLoading: Boolean,
    apiResponseMessage: String?,
    viewModel: CompanyDataViewModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top=16.dp), // Padding consistente
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Sentimento",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFF1E2B4D)
            )

            if (isLoading && aggregatedFeelingData == null) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (aggregatedFeelingData.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(apiResponseMessage ?: "Nenhum dado de sentimento disponível para o gráfico.")
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                                    // Configurações para valores DENTRO das fatias (percentuais)
                                    setDrawEntryLabels(false) // Remove rótulos de categoria das fatias
                                    animateY(1200, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { pieChart ->
                                val entries = ArrayList<PieEntry>()
                                aggregatedFeelingData.forEach { feedbackItem ->
                                    entries.add(PieEntry(feedbackItem.totalCount?.toFloat() ?: 0f, feedbackItem.feeling ?: "N/A"))
                                }

                                if (entries.isNotEmpty()) {
                                    val dataSet = PieDataSet(entries, "")
                                    dataSet.colors = feelingPieChartColors.take(entries.size) + ColorTemplate.MATERIAL_COLORS.toList().drop(feelingPieChartColors.size).take(entries.size - feelingPieChartColors.size)
                                    dataSet.sliceSpace = 2f
                                    dataSet.valueTextColor = AndroidColor.DKGRAY // Percentuais em cor escura para contraste
                                    dataSet.valueTextSize = 12f // Tamanho dos percentuais
                                    dataSet.valueFormatter = PercentFormatter(pieChart)
                                    dataSet.setDrawValues(true) // Mostrar valores (percentuais) nas fatias

                                    val pieData = PieData(dataSet)
                                    pieChart.data = pieData
                                    pieChart.setUsePercentValues(true)
                                    pieChart.invalidate()

                                    pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                                        override fun onValueSelected(e: ChartEntry?, h: Highlight?) {
                                            if (e is PieEntry) {
                                                val clickedData = aggregatedFeelingData.find { it.feeling == e.label }
                                                viewModel.selectFeelingSlice(clickedData) // Nova função no ViewModel
                                            }
                                        }
                                        override fun onNothingSelected() {
                                            viewModel.selectFeelingSlice(null) // Nova função
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
                    Column(modifier = Modifier.weight(0.4f)) {
                        aggregatedFeelingData.forEachIndexed { index, feedbackItem ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.selectFeelingSlice(feedbackItem) }
                            ) {
                                // Usando um Box colorido para a legenda do sentimento
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(getFeelingLegendColor(index))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${feedbackItem.feeling ?: "N/A"} (${feedbackItem.totalCount ?: 0})",
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
fun FeelingSliceDetailsPopup( // Novo Popup específico ou um genérico se preferir
    sliceData: AggregatedFeelingFeedbackDTO,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalhes para Sentimento: ${sliceData.feeling ?: "N/A"}") },
        text = {
            Column {
                Text("Total de respostas para este sentimento: ${sliceData.totalCount ?: 0}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Distribuição de Níveis:", style = MaterialTheme.typography.titleSmall)
                sliceData.levels?.forEach { levelDetail -> // Reutilizando EmojiLevelCountDTO
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