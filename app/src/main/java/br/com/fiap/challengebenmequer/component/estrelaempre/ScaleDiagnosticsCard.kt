// Arquivo: component/estrelaempre/ScaleDiagnosticsCard.kt
package br.com.fiap.challengebenmequer.component.estrelaempre // Ou seu novo pacote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import br.com.fiap.challengebenmequer.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.challengebenmequer.dto.AggregatedScaleQuestionDTO
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun ScaleDiagnosticsCard(
    modifier: Modifier = Modifier,
    title: String,
    diagnosticsData: List<AggregatedScaleQuestionDTO>?,
    isLoading: Boolean,
    apiResponseMessage: String? // Para exibir mensagens de erro ou status
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
        shape = RoundedCornerShape(16.dp), // Borda arredondada como na imagem
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) // Fundo branco para o card
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
            )

            if (isLoading && diagnosticsData == null) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (diagnosticsData.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
                    Text(apiResponseMessage ?: "Nenhum dado de diagnóstico disponível.")
                }
            } else {
                diagnosticsData.forEachIndexed { index, diagnosticItem ->
                    DiagnosticRow(diagnosticItem = diagnosticItem)
                    if (index < diagnosticsData.size - 1) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DiagnosticRow(diagnosticItem: AggregatedScaleQuestionDTO) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = diagnosticItem.questionText ?: "Pergunta não disponível",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f).padding(end = 8.dp) // Ocupa mais espaço
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StarRating(rating = diagnosticItem.averageScore ?: 0.0)
            Text(
                text = "${diagnosticItem.totalResponses ?: 0} avaliações",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp, // Fonte menor como na imagem
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StarRating(rating: Double, maxStars: Int = 5, starSize: Dp = 20.dp) { // Adicionado starSize como parâmetro
    Row(verticalAlignment = Alignment.CenterVertically) { // Adicionado alinhamento vertical
        val fullStars = floor(rating).toInt()
        val hasHalfStar = (rating - fullStars) >= 0.25 && (rating - fullStars) < 0.75
        val roundedUpFullStars = ceil(rating).toInt() // Para calcular estrelas vazias corretamente com meia estrela
        val actualFullStars = if (hasHalfStar) fullStars else roundedUpFullStars // Se não tem meia, o arredondamento já conta como cheia
        val emptyStars = maxStars - actualFullStars - (if (hasHalfStar) 1 else 0)


        // Estrelas Cheias
        repeat(fullStars) { // Sempre desenha o 'floor' de estrelas cheias
            Image(
                painter = painterResource(id = R.drawable.ic_star_filled), // Sua imagem de estrela cheia
                contentDescription = "Estrela Cheia",
                modifier = Modifier.size(starSize)
            )
        }

        // Meia Estrela (se aplicável)
        if (hasHalfStar) {
            Image(
                painter = painterResource(id = R.drawable.ic_star_half), // Sua imagem de meia estrela
                contentDescription = "Meia Estrela",
                modifier = Modifier.size(starSize)
            )
        }

        // Estrelas Vazias
        // O cálculo de estrelas vazias precisa considerar se uma meia estrela foi desenhada
        val starsToDrawAsEmpty = maxStars - fullStars - (if (hasHalfStar) 1 else 0)
        repeat(starsToDrawAsEmpty) {
            Image(
                painter = painterResource(id = R.drawable.ic_star_border), // Sua imagem de estrela vazia
                contentDescription = "Estrela Vazia",
                modifier = Modifier.size(starSize)
            )
        }
    }
}