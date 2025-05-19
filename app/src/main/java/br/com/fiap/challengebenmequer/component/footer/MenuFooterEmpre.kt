package br.com.fiap.challengebenmequer.component.footer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.fiap.challengebenmequer.R

@Composable
fun MenuFooterEmpre(onNavigate: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .drawBehind {
                drawRect(
                    color = Color.Black.copy(alpha = 0.1f),
                    topLeft = Offset(0f, -8f), // desloca pra cima
                    size = Size(size.width, 5f) // altura da "sombra"
                )
            },
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FooterButton(
                iconRes = R.drawable.icon_header_emocao,
                label = stringResource(id = R.string.footer_empre_emotion),
                onClick = { onNavigate("emocaoempre") }
            )
            FooterButton(
                iconRes = R.drawable.icon_header_questionario,
                label = stringResource(id = R.string.footer_empre_questionnaire),
                onClick = { onNavigate("questionarioempre") }
            )
            FooterButton(
                iconRes = R.drawable.icon_estrela,
                label = stringResource(id = R.string.footer_empre_star),
                onClick = { onNavigate("estrelaempre") }
            )
        }
    }
}
