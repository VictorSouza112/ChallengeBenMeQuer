package br.com.fiap.challengebenmequer.component.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun ChatbotModalFunc(navController: NavController) {
    val context = LocalContext.current
    val rootNode = remember { buildChatTree(context) }
    var showIntro by remember { mutableStateOf(true) }
    var currentNode by remember { mutableStateOf<ChatNode?>(null) }
    val chatMessages = remember { mutableStateListOf<ChatMessage>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color =  colorResource(id = R.color.azul))
    ) {
        MenuHeaderChatbot(navController)

        if (showIntro) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.chatbot_user),
                        contentDescription = "Ben",
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        text = stringResource(R.string.chatbot_func_ben1) + "\n" + stringResource(R.string.chatbot_func_ben2),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))

                // botões iniciais
                rootNode.options.forEach { option ->
                    Button(
                        onClick = {
                            showIntro = false
                            chatMessages.add(ChatMessage.Sent(option.label))
                            chatMessages.add(ChatMessage.Received(option.next.text))
                            currentNode = option.next
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.verde)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color.Black.copy(alpha = 0.8f),
                                spotColor = Color.Black.copy(alpha = 0.8f)
                            )
                    ) {
                        Text(text = option.label, color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        } else {
            // lista de mensagens
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(chatMessages) { message ->
                    ChatBubble(message)
                }
            }

            // botões de opção do nó atual
            currentNode?.options?.forEach { option ->
                Button(
                    onClick = {
                        chatMessages.add(ChatMessage.Sent(option.label))
                        chatMessages.add(ChatMessage.Received(option.next.text))
                        currentNode = option.next
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.verde))
                ) {
                    Text(text = option.label, color = Color.White)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Balão de mensagem (sem alterações no design que você já validou)
@Composable
fun ChatBubble(
    message: ChatMessage,
    userAvatarRes: Int = R.drawable.avatar_1, // avatar padrão (pode trocar)
    botAvatarRes: Int = R.drawable.chatbot_user      // avatar do Ben
) {
    val isUser = message is ChatMessage.Sent
    val icon = if (isUser) userAvatarRes else botAvatarRes
    val backgroundColor = if (isUser) Color.White else Color.LightGray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        if (!isUser) {
            AvatarBubble(icon)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = if (isUser)
                RoundedCornerShape(topStart = 16.dp, topEnd = 0.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
            else
                RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            color = backgroundColor,
            tonalElevation = 4.dp,
            modifier = Modifier
                .widthIn(100.dp, 260.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.8f),
                    spotColor = Color.Black.copy(alpha = 0.8f)
                )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = Color.Black
            )
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            AvatarBubble(icon)
        }
    }
}

@Composable
fun AvatarBubble(imageRes: Int) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(2.dp, Color.Black, CircleShape)
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Avatar",
            modifier = Modifier.fillMaxSize()
        )
    }
}
