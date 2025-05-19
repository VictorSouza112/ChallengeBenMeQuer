// Arquivo: component/questionarioempre/AlertSignsFeedbackCard.kt
package br.com.fiap.challengebenmequer.component.questionarioempre

// Copie TODOS os imports do WorkloadFeedbackCard.kt aqui
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.fiap.challengebenmequer.dto.AggregatedWorkloadQuestionDTO // Reutilizando este DTO
import br.com.fiap.challengebenmequer.dto.WorkloadAnswerStatsItemDTO   // Reutilizando este DTO
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

// Cores para o gráfico
val alertSignsChartColors = listOf(
    Color(0xFFADD8E6),
    Color(0xFF87CEEB),
    Color(0xFF4682B4),
    Color(0xFF90EE90),
    Color(0xFF32CD32)
).map { it.toArgb() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertSignsFeedbackCard(
    modifier: Modifier = Modifier,
    allAlertSignsQuestionsData: List<AggregatedWorkloadQuestionDTO>?, // Dados das perguntas de Sinais de Alerta
    isLoading: Boolean,
    apiResponseMessage: String?,
    viewModel: CompanyDataViewModel
) {
    var selectedQuestionIndex by remember { mutableStateOf(0) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val currentQuestionData = allAlertSignsQuestionsData?.getOrNull(selectedQuestionIndex)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Sinais de Alerta", // Título do Card
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color(0xFF1E2B4D)
            )

            if (!allAlertSignsQuestionsData.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = currentQuestionData?.questionText ?: "Selecione uma pergunta",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pergunta sobre Sinais de Alerta") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xFF1E2B4D)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            allAlertSignsQuestionsData.forEachIndexed { index, questionDto ->
                                DropdownMenuItem(
                                    text = { Text(questionDto.questionText ?: "Pergunta ${index + 1}") },
                                    onClick = {
                                        selectedQuestionIndex = index
                                        dropdownExpanded = false
                                        viewModel.selectAlertSignSlice(null) // Limpa seleção de fatia
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (isLoading && currentQuestionData == null && (allAlertSignsQuestionsData?.isNotEmpty() == true)) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (currentQuestionData == null || currentQuestionData.answerStats.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(if (allAlertSignsQuestionsData.isNullOrEmpty()) apiResponseMessage ?: "Carregando dados..." else "Selecione uma pergunta ou dados não disponíveis.")
                }
            } else {
                // Gráfico e Legenda (código muito similar ao WorkloadFeedbackCard)
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
                                    setDrawEntryLabels(false)
                                    animateY(1200, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { pieChart ->
                                val entries = ArrayList<PieEntry>()
                                currentQuestionData.answerStats.forEach { answerStat ->
                                    entries.add(PieEntry(answerStat.totalCountForAnswer?.toFloat() ?: 0f, answerStat.answerValue ?: "N/A"))
                                }

                                if (entries.isNotEmpty()) {
                                    val dataSet = PieDataSet(entries, "")
                                    // Usando alertSignsChartColors ou reutilizando workloadChartColors
                                    dataSet.colors = alertSignsChartColors.take(entries.size) + ColorTemplate.PASTEL_COLORS.toList().drop(alertSignsChartColors.size).take(entries.size - alertSignsChartColors.size)
                                    dataSet.sliceSpace = 2f
                                    dataSet.valueTextColor = AndroidColor.DKGRAY
                                    dataSet.valueTextSize = 12f
                                    dataSet.valueFormatter = PercentFormatter(pieChart)
                                    dataSet.setDrawValues(true)

                                    val pieData = PieData(dataSet)
                                    pieChart.data = pieData
                                    pieChart.setUsePercentValues(true)
                                    pieChart.invalidate()

                                    pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                                        override fun onValueSelected(e: ChartEntry?, h: Highlight?) {
                                            if (e is PieEntry) {
                                                val clickedData = currentQuestionData.answerStats.find { it.answerValue == e.label }
                                                viewModel.selectAlertSignSlice(clickedData) // Nova função no ViewModel
                                            }
                                        }
                                        override fun onNothingSelected() {
                                            viewModel.selectAlertSignSlice(null)
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
                        currentQuestionData.answerStats.forEachIndexed { index, answerStat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.selectAlertSignSlice(answerStat) }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color(alertSignsChartColors.getOrElse(index) { ColorTemplate.getHoloBlue() }))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${answerStat.answerValue ?: "N/A"} (${answerStat.totalCountForAnswer ?: 0})",
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

// Popup específico para Sinais de Alerta
@Composable
fun AlertSignSliceDetailsPopup(
    sliceData: WorkloadAnswerStatsItemDTO, // Reutilizando DTO para a estrutura da fatia
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalhes para: ${sliceData.answerValue ?: "N/A"}") },
        text = {
            Column {
                Text("Total de respostas para esta opção: ${sliceData.totalCountForAnswer ?: 0}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Distribuição de Níveis:", style = MaterialTheme.typography.titleSmall)
                sliceData.levelDistribution?.forEach { levelDetail ->
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