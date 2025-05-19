package br.com.fiap.challengebenmequer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.component.footer.MenuFooterEmpre
import br.com.fiap.challengebenmequer.component.footer.MenuFooterFunc
import br.com.fiap.challengebenmequer.component.header.MenuHeaderFunc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionarioFuncScreen(navController: NavController) {
    var testeState by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.cinza))
    ) {
        MenuHeaderFunc(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 78.dp, start = 20.dp, end = 20.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize(),
                value = testeState,
                onValueChange = { testeState = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Texto clicável
            Text(
                text = stringResource(id = R.string.questionnaire_func_hello),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul)
            )
        }
        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
        ) {
            MenuFooterFunc(onNavigate = { route -> navController.navigate(route) })
        }
    }
}