// Arquivo: screen/EmocaoEmpreScreen.kt
package br.com.fiap.challengebenmequer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.component.footer.MenuFooterEmpre
import br.com.fiap.challengebenmequer.component.header.MenuHeaderEmpre
import br.com.fiap.challengebenmequer.component.emojiempre.EmojiFeedbackCard
import br.com.fiap.challengebenmequer.component.emojiempre.EmojiSliceDetailsPopup
import br.com.fiap.challengebenmequer.component.emojiempre.FeelingFeedbackCard
import br.com.fiap.challengebenmequer.component.emojiempre.FeelingSliceDetailsPopup
import br.com.fiap.challengebenmequer.viewmodel.CompanyDataViewModel

@Composable
fun EmocaoEmpreScreen(
    navController: NavController,
    companyViewModel: CompanyDataViewModel = viewModel()
) {
    val aggregatedEmojiData by companyViewModel.aggregatedEmojiFeedback
    val selectedEmojiSlice by companyViewModel.selectedEmojiSliceForPopup
    val aggregatedFeelingData by companyViewModel.aggregatedFeelingFeedback
    val selectedFeelingSlice by companyViewModel.selectedFeelingSliceForPopup
    val isLoading by companyViewModel.isLoading
    val apiResponseMessage by companyViewModel.apiResponseMessage

    LaunchedEffect(Unit) {
        if (companyViewModel.aggregatedEmojiFeedback.value == null) { // Condicional para buscar
            companyViewModel.fetchAggregatedEmojiFeedback()
        }
        if (companyViewModel.aggregatedFeelingFeedback.value == null) { // Condicional para buscar
            companyViewModel.fetchAggregatedFeelingFeedback()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.cinza)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MenuHeaderEmpre()

            EmojiFeedbackCard(
                aggregatedEmojiData = aggregatedEmojiData,
                isLoading = isLoading,
                apiResponseMessage = apiResponseMessage,
                viewModel = companyViewModel
            )

            // Adicionando o Card de Feedback de Sentimento
            FeelingFeedbackCard(
                aggregatedFeelingData = aggregatedFeelingData,
                isLoading = isLoading,
                apiResponseMessage = apiResponseMessage,
                viewModel = companyViewModel
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
        ) {
            MenuFooterEmpre(onNavigate = { route -> navController.navigate(route) })
        }

        // Botão "Gerar insights" fixo acima do footer
        ChatbotEmpre(
            onClick = { navController.navigate("chatbotempre") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 96.dp)
        )

        // O Popup é controlado aqui, na tela principal, com base no estado do ViewModel
        selectedEmojiSlice?.let { slice ->
            EmojiSliceDetailsPopup(
                sliceData = slice,
                onDismiss = { companyViewModel.selectEmojiSlice(null) }
            )
        }

        // Popup para detalhes do Sentimento
        selectedFeelingSlice?.let { slice ->
            FeelingSliceDetailsPopup(
                sliceData = slice,
                onDismiss = { companyViewModel.selectFeelingSlice(null) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotEmpre(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(60.dp)
            .wrapContentWidth()
            .border(2.dp, Color.White, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = colorResource(id = R.color.verde),
        shadowElevation = 8.dp,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.chatbot_empre),
                contentDescription = "Mascote",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Gerar", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text("insights", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}