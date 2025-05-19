package br.com.fiap.challengebenmequer.component.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.component.header.MenuHeaderChatbot

@Composable
fun ChatbotModalEmpre(navController: NavController) {
    val context = LocalContext.current
    val chatMessages = remember { mutableStateListOf<ChatMessage>() }

    val options = listOf(
        stringResource(id = R.string.chatbot_empre_anxiety) to stringResource(id = R.string.chatbot_empre_anxiety_response),
        stringResource(id = R.string.chatbot_empre_depression) to stringResource(id = R.string.chatbot_empre_depression_response),
        stringResource(id = R.string.chatbot_empre_burnout) to stringResource(id = R.string.chatbot_empre_burnout_response)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.azul))
    ) {
        MenuHeaderChatbot(navController)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            if (chatMessages.isEmpty()) {
                // Tela inicial com Ben e texto centralizado
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chatbot_empre),
                        contentDescription = "Ben",
                        modifier = Modifier
                            .size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.chatbot_empre_ben),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(chatMessages) { message ->
                        ChatBubble(
                            message = message,
                            userAvatarRes = R.drawable.chatbot_softtek,
                            botAvatarRes = R.drawable.chatbot_empre
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botões de opções sempre visíveis
            Column {
                options.forEach { (label, response) ->
                    Button(
                        onClick = {
                            chatMessages.add(ChatMessage.Sent(label))
                            chatMessages.add(ChatMessage.Received(response))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.verde)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(90.dp)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color.Black.copy(alpha = 0.8f),
                                spotColor = Color.Black.copy(alpha = 0.8f)
                            )
                    ) {
                        Text(text = label, color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
