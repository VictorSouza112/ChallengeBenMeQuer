package br.com.fiap.challengebenmequer.component.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R

@Composable
fun MenuHeaderChatbot(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .background(Color.White) // aplique o mesmo shape
            .height(70.dp)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logotipo_sem_circulo),
            contentDescription = "Logotipo",
            modifier = Modifier.size(70.dp)
        )
        IconButton(onClick = {
            navController.popBackStack() // ← volta à tela anterior
        }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Fechar",
                tint = colorResource(id = R.color.azul)
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}
