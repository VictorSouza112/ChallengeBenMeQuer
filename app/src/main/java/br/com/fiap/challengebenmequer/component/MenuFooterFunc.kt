package br.com.fiap.challengebenmequer.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.challengebenmequer.R

@Composable
fun MenuFooterFunc(onNavigate: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Texto clicável
            Text(
                text = stringResource(id = R.string.footer_func_dashboard),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onNavigate("dashboardfunc")
                }
            )
            // Texto clicável
            Text(
                text = stringResource(id = R.string.footer_func_questionnaire),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onNavigate("questionariofunc")
                }
            )
            // Texto clicável
            Text(
                text = stringResource(id = R.string.footer_func_emotion),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onNavigate("emocaofunc")
                }
            )
            // Texto clicável
            Text(
                text = stringResource(id = R.string.footer_func_calendar),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onNavigate("calendariofunc")
                }
            )
        }
    }
}