package br.com.fiap.challengebenmequer.component.footer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.challengebenmequer.R


@Composable
fun MenuFooterFunc(onNavigate: (String) -> Unit) {
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
                iconRes = R.drawable.icon_header_home,
                label = stringResource(id = R.string.footer_func_dashboard),
                onClick = { onNavigate("dashboardfunc") }
            )
            FooterButton(
                iconRes = R.drawable.icon_header_questionario,
                label = stringResource(id = R.string.footer_func_questionnaire),
                onClick = { onNavigate("questionariofunc") }
            )
            FooterButton(
                iconRes = R.drawable.icon_header_emocao,
                label = stringResource(id = R.string.footer_func_emotion),
                onClick = { onNavigate("emocaofunc") }
            )
            FooterButton(
                iconRes = R.drawable.icon_header_calendario,
                label = stringResource(id = R.string.footer_func_calendar),
                onClick = { onNavigate("calendariofunc") }
            )
        }
    }
}

@Composable
fun FooterButton(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = colorResource(id = R.color.azul_escuro)
        )
    }
}
