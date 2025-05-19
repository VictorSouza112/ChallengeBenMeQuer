package br.com.fiap.challengebenmequer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.component.footer.MenuFooterEmpre
import br.com.fiap.challengebenmequer.component.header.MenuHeaderEmpre
import br.com.fiap.challengebenmequer.component.questionarioempre.AlertSignSliceDetailsPopup
import br.com.fiap.challengebenmequer.component.questionarioempre.AlertSignsFeedbackCard
import br.com.fiap.challengebenmequer.component.questionarioempre.WorkloadFeedbackCard
import br.com.fiap.challengebenmequer.component.questionarioempre.WorkloadSliceDetailsPopup
import br.com.fiap.challengebenmequer.viewmodel.CompanyDataViewModel

@Composable
fun QuestionarioEmpreScreen(
    navController: NavController,
    companyViewModel: CompanyDataViewModel = viewModel()
) {
    val aggregatedWorkloadData by companyViewModel.aggregatedWorkloadFeedback
    val selectedWorkloadSlice by companyViewModel.selectedWorkloadSliceForPopup
    val aggregatedAlertSignsData by companyViewModel.aggregatedAlertSignsFeedback
    val selectedAlertSignSlice by companyViewModel.selectedAlertSignSliceForPopup
    val isLoading by companyViewModel.isLoading
    val apiResponseMessage by companyViewModel.apiResponseMessage

    LaunchedEffect(Unit) {
        if (aggregatedWorkloadData == null) {
            companyViewModel.fetchAggregatedWorkloadFeedback()
        }
        if (companyViewModel.aggregatedAlertSignsFeedback.value == null) { // Busca dados de Sinais de Alerta
            companyViewModel.fetchAggregatedAlertSignsFeedback()
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

            // Usando o Card de Feedback de Carga de Trabalho
            WorkloadFeedbackCard(
                allWorkloadQuestionsData = aggregatedWorkloadData,
                isLoading = isLoading,
                apiResponseMessage = apiResponseMessage,
                viewModel = companyViewModel
            )

            // Adicionando o Card de Feedback de Sinais de Alerta
            AlertSignsFeedbackCard(
                allAlertSignsQuestionsData = aggregatedAlertSignsData,
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

        // O Popup é controlado aqui, na tela principal, com base no estado do ViewModel
        selectedWorkloadSlice?.let { slice ->
            WorkloadSliceDetailsPopup(
                sliceData = slice,
                onDismiss = { companyViewModel.selectWorkloadSlice(null) }
            )
        }

        // Popup para Sinais de Alerta
        selectedAlertSignSlice?.let { slice ->
            // Se você criou AlertSignSliceDetailsPopup:
            AlertSignSliceDetailsPopup(
                sliceData = slice,
                onDismiss = { companyViewModel.selectAlertSignSlice(null) }
            )
        }
    }
}