package br.com.fiap.challengebenmequer.component.questionarioempre

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
import br.com.fiap.challengebenmequer.dto.AggregatedWorkloadQuestionDTO
import br.com.fiap.challengebenmequer.dto.WorkloadAnswerStatsItemDTO
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

// Cores para o gráfico
val workloadChartColors = listOf(
    Color(0xFFADD8E6),
    Color(0xFF87CEEB),
    Color(0xFF4682B4),
    Color(0xFF90EE90),
    Color(0xFF32CD32)
).map { it.toArgb() }

@OptIn(ExperimentalMaterial3Api::class) // Necessário para ExposedDropdownMenuBox
@Composable
fun WorkloadFeedbackCard(
    modifier: Modifier = Modifier,
    allWorkloadQuestionsData: List<AggregatedWorkloadQuestionDTO>?,
    isLoading: Boolean,
    apiResponseMessage: String?,
    viewModel: CompanyDataViewModel
) {
    // Estado para controlar qual pergunta está selecionada (índice)
    var selectedQuestionIndex by remember { mutableStateOf(0) }
    // Estado para controlar se o menu dropdown está expandido
    var dropdownExpanded by remember { mutableStateOf(false) }

    val currentQuestionData = allWorkloadQuestionsData?.getOrNull(selectedQuestionIndex)

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
                text = "Fatores de Carga de Trabalho",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color(0xFF1E2B4D)
            )

            // Dropdown para selecionar a pergunta
            if (!allWorkloadQuestionsData.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                    ) {
                        OutlinedTextField( // Usando OutlinedTextField para um visual consistente
                            value = currentQuestionData?.questionText ?: "Selecione uma pergunta",
                            onValueChange = { /* Não fazemos nada aqui, pois é apenas display */ },
                            readOnly = true, // Impede a digitação
                            label = { Text("Pergunta sobre Carga de Trabalho") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor() // Importante para o ExposedDropdownMenuBox
                                .fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xFF1E2B4D)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            allWorkloadQuestionsData.forEachIndexed { index, questionDto ->
                                DropdownMenuItem(
                                    text = { Text(questionDto.questionText ?: "Pergunta ${index + 1}") },
                                    onClick = {
                                        selectedQuestionIndex = index
                                        dropdownExpanded = false
                                        viewModel.selectWorkloadSlice(null) // Limpa seleção de fatia ao mudar pergunta
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (isLoading && currentQuestionData == null && (allWorkloadQuestionsData?.isNotEmpty() == true)) {
                // Mostra loading apenas se estamos esperando dados para a pergunta selecionada
                Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (currentQuestionData == null || currentQuestionData.answerStats.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(if (allWorkloadQuestionsData.isNullOrEmpty()) apiResponseMessage ?: "Carregando dados..." else "Selecione uma pergunta ou dados não disponíveis.")
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
                                    dataSet.colors = workloadChartColors.take(entries.size) + ColorTemplate.JOYFUL_COLORS.toList().drop(workloadChartColors.size).take(entries.size - workloadChartColors.size)
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
                                                viewModel.selectWorkloadSlice(clickedData)
                                            }
                                        }
                                        override fun onNothingSelected() {
                                            viewModel.selectWorkloadSlice(null)
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
                                    .clickable { viewModel.selectWorkloadSlice(answerStat) }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color(workloadChartColors.getOrElse(index) { ColorTemplate.getHoloBlue() }))
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

@Composable
fun WorkloadSliceDetailsPopup(
    sliceData: WorkloadAnswerStatsItemDTO, // Certifique-se que este é o tipo correto
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