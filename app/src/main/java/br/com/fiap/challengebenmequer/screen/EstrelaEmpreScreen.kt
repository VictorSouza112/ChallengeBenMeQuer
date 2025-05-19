package br.com.fiap.challengebenmequer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.component.footer.MenuFooterEmpre
import br.com.fiap.challengebenmequer.component.header.MenuHeaderEmpre
import br.com.fiap.challengebenmequer.component.estrelaempre.ScaleDiagnosticsCard
import br.com.fiap.challengebenmequer.viewmodel.CompanyDataViewModel

@Composable
fun EstrelaEmpreScreen(
    navController: NavController,
    companyViewModel: CompanyDataViewModel = viewModel()
) {
    val climateData by companyViewModel.aggregatedClimateDiagnostics
    val communicationData by companyViewModel.aggregatedCommunicationDiagnostics
    val leadershipData by companyViewModel.aggregatedLeadershipDiagnostics

    val isLoading by companyViewModel.isLoading
    val apiResponseMessage by companyViewModel.apiResponseMessage // Para mensagens de erro gerais

    LaunchedEffect(Unit) {
        if (climateData == null) companyViewModel.fetchAggregatedClimateDiagnostics()
        if (communicationData == null) companyViewModel.fetchAggregatedCommunicationDiagnostics()
        if (leadershipData == null) companyViewModel.fetchAggregatedLeadershipDiagnostics()
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

            ScaleDiagnosticsCard(
                title = "Diagnóstico de Clima",
                diagnosticsData = climateData,
                isLoading = isLoading && climateData == null, // Mostra loading para este card específico
                apiResponseMessage = if (climateData == null) apiResponseMessage else null
            )

            ScaleDiagnosticsCard(
                title = "Comunicação",
                diagnosticsData = communicationData,
                isLoading = isLoading && communicationData == null,
                apiResponseMessage = if (communicationData == null) apiResponseMessage else null
            )

            ScaleDiagnosticsCard(
                title = "Relação com a Liderança",
                diagnosticsData = leadershipData,
                isLoading = isLoading && leadershipData == null,
                apiResponseMessage = if (leadershipData == null) apiResponseMessage else null
            )

            Spacer(modifier = Modifier.weight(1f)) // Empurra o footer para baixo
        }

        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
        ) {
            MenuFooterEmpre(onNavigate = { route -> navController.navigate(route) })
        }
    }
}