package br.com.fiap.challengebenmequer.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.component.footer.MenuFooterFunc
import br.com.fiap.challengebenmequer.component.header.MenuHeaderFunc
import br.com.fiap.challengebenmequer.data.repository.SessionRepository
import br.com.fiap.challengebenmequer.dto.CheckinSummaryDTO
import br.com.fiap.challengebenmequer.viewmodel.UserDashboardViewModel

@Composable
fun DashboardFuncScreen(
    navController: NavController,
    userDashboardViewModel: UserDashboardViewModel = viewModel()
) {
    // Observa os estados do UserDashboardViewModel
    val isLoading by userDashboardViewModel.isLoading
    val apiResponseMessage by userDashboardViewModel.apiResponseMessage
    val checkinSummary by userDashboardViewModel.checkinSummary
    // Vamos adicionar um log para verificar se o token está sendo pego
    LaunchedEffect(Unit) { // Re-executa se o token mudar
        Log.d("DashboardFuncScreen", "Verificando token para buscar dados: ${SessionRepository.getToken()}")
        userDashboardViewModel.fetchCheckinSummary()
    }

    Box(modifier = Modifier.fillMaxSize().background(color = colorResource(id = R.color.cinza))) {
        // Conteúdo rolável por baixo do footer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 78.dp, start = 20.dp, end = 20.dp) // deixa espaço para header
        ) {
            CardQuestionario(navController)
            Spacer(modifier = Modifier.height(20.dp))
            ComoVoceEsta(navController)
            Spacer(modifier = Modifier.height(20.dp))
            UltimaSemana( // Passando os novos parâmetros
                summary = checkinSummary,
                isLoading = isLoading,
                apiResponseMessage = apiResponseMessage
            )
            Spacer(modifier = Modifier.height(20.dp))
            CalendarioSemanal()
            Spacer(modifier = Modifier.height(20.dp))
            LinhaDoTempo()
            Spacer(modifier = Modifier.height(100.dp)) // espaço para rodapé
        }

        // Header fixo
        MenuHeaderFunc(navController)

        // Footer fixo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            MenuFooterFunc(onNavigate = { route -> navController.navigate(route) })
        }

        // Chatbot FAB acima do footer
        ChatbotUser(
            onClick = { navController.navigate("chatbotfunc") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 96.dp)
        )
    }
}

@Composable
fun CardQuestionario(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(2.dp, Color.White, RoundedCornerShape(10.dp))
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .clickable { navController.navigate("questionariofunc") },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.verde))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_questionario),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.dashboard_func_questionnaire),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.W400
            )
        }
    }
}

@Composable
fun ComoVoceEsta(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(2.dp, Color.White, RoundedCornerShape(10.dp)) // borda branca
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .clickable { navController.navigate("emocaofunc") },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.azul))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logotipo_sem_circulo),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text =  stringResource(id = R.string.dashboard_func_emotion),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.W400
            )
        }
    }
}

@Composable
fun UltimaSemana(
    summary: CheckinSummaryDTO?,
    isLoading: Boolean,
    apiResponseMessage: String?
) {
    val emojiDisplayData = remember(summary?.predominantEmoji) {
        val emoji = summary?.predominantEmoji?.uppercase()
        when (emoji) {
            "ALEGRE" -> Triple("Alegre", R.drawable.avatar_1, R.color.verde_alegre)
            "TRISTE" -> Triple("Triste", R.drawable.avatar_1_triste, R.color.azul_triste)
            "RAIVA" -> Triple("Bravo", R.drawable.avatar_1_bravo, R.color.vermelho_bravo)
            "ANSIOSO" -> Triple("Ansioso", R.drawable.avatar_1_ansioso, R.color.laranja_ansioso)
            "CANSADO" -> Triple("Cansado", R.drawable.avatar_1_cansado, R.color.cinza_cansado)
            "MEDO" -> Triple("Medo", R.drawable.avatar_1_medo, R.color.roxo_medo)
            else -> Triple("Alegre", R.drawable.avatar_1, R.color.verde_alegre) // Padrão se nulo ou desconhecido
        }
    }

    val (emojiText, emojiImageRes, emojiBackgroundColorRes) = emojiDisplayData

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp) // Usar defaultMinSize para permitir que o card cresça se o conteúdo do erro for grande
            .border(2.dp, Color.White, RoundedCornerShape(10.dp))
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 1f),
                spotColor = Color.Black.copy(alpha = 1f)
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.verde))
    ) {
        // Adicionando lógica de isLoading e apiResponseMessage
        if (isLoading && summary == null) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (summary != null) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_func_last_week1) + "\n" + stringResource(R.string.dashboard_func_last_week2),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = emojiBackgroundColorRes),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(8.dp),
                                ambientColor = Color.Black.copy(alpha = 0.5f),
                                spotColor = Color.Black.copy(alpha = 0.5f)
                            )
                    ) {
                        Text(
                            text = emojiText,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Image(
                    painter = painterResource(id = emojiImageRes),
                    contentDescription = "Avatar da emoção da última semana: $emojiText",
                    modifier = Modifier.size(90.dp)
                )
            }
        } else if (!isLoading && apiResponseMessage != null) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = apiResponseMessage,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        } else { // Caso padrão (ex: sem dados e sem mensagem de erro específica para este card)
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text("Resumo da última semana não disponível.", color = Color.White, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun CalendarioSemanal() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .border(2.dp, Color.White, RoundedCornerShape(10.dp)) // borda branca
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.azul))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Coluna com os chips de dias da semana
            Column(
                modifier = Modifier.fillMaxWidth(0.68f), // reduz para abrir espaço para a coluna da direita
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val daysTop = stringArrayResource(id = R.array.days_top).toList()
                val daysBottom = stringArrayResource(id = R.array.days_bottom).toList()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    daysTop.forEachIndexed { index, d ->
                        DiaChip(d)
                        if (index != daysTop.lastIndex) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    daysBottom.forEach { d -> DiaChip(d) }
                }
            }

            // Coluna da direita com dropdown e botão
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var expanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(8.dp),
                            ambientColor = Color.Black.copy(alpha = 0.8f),
                            spotColor = Color.Black.copy(alpha = 0.8f)
                        )
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        stringResource(id = R.string.dashboard_func_calendar_filter_week),
                        color = colorResource(id = R.color.azul_claro),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (1..4).forEach { week ->
                            DropdownMenuItem(
                                text = { Text(stringResource(id = R.string.dashboard_func_calendar_dropdown_week, week), color = colorResource(id = R.color.azul)) },
                                onClick = { expanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(8.dp),
                            ambientColor = Color.Black.copy(alpha = 0.8f),
                            spotColor = Color.Black.copy(alpha = 0.8f)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_calendario_dash),
                        contentDescription = "Calendário",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun DiaChip(dia: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.8f)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = dia,
                fontSize = 12.sp,
                color = colorResource(id = R.color.azul_claro)
            )
            Spacer(Modifier.height(4.dp))
            val context = LocalContext.current
            val diasAvatar = context.resources.getStringArray(R.array.dashboard_func_day_chip).toList()

            if (dia in diasAvatar) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_1_bravo),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            } else {
                Spacer(Modifier.height(16.dp))
            }

        }
    }
}

@Composable
fun ChatbotUser(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .size(64.dp) // define o tamanho do botão
            .border(2.dp, Color.White, CircleShape), // borda branca
        shape = CircleShape, // forma circular
        containerColor = Color(0xFFA8CBB7),
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.chatbot_user),
            contentDescription = "Mascote",
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
fun LinhaDoTempo() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFA8CBB7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(2.dp, Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Título centralizado e dropdown à direita
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.dashboard_func_time_line),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )

                var expanded by remember { mutableStateOf(false) }
                var selectedMonth by remember { mutableStateOf("Janeiro") }
                val context = LocalContext.current
                val meses = context.resources.getStringArray(R.array.dashboard_func_time_line_months).toList()


                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(8.dp),
                            ambientColor = Color.Black.copy(alpha = 0.8f),
                            spotColor = Color.Black.copy(alpha = 0.8f)
                        )
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .align(Alignment.CenterEnd)
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        selectedMonth + " ▾",
                        color = Color(0xFF1E2B4D),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        meses.forEach { mes ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        mes,
                                        color = Color(0xFF6C9BD2),
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                onClick = {
                                    selectedMonth = mes
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Linha do tempo com alinhamento dos chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                val semanas = context.resources.getStringArray(R.array.dashboard_func_time_line_weeks).toList()
                val emojis = listOf(
                    R.drawable.emoji_feliz,
                    R.drawable.emoji_cansado,
                    R.drawable.emoji_ansioso,
                    R.drawable.emoji_bravo
                )
                val descricoes = listOf("Feliz", "Cansado", "Ansioso", "Bravo")

                semanas.forEachIndexed { index, semana ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Sempre reservar espaço acima (mesmo que esteja vazio)
                        if (index % 2 == 0) {
                            Text(
                                text = semana,
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            // Espaço invisível para manter alinhamento
                            Text(
                                text = stringResource(id = R.string.dashboard_func_calendar_week_x), // texto fictício com tamanho equivalente
                                fontSize = 10.sp,
                                color = Color.Transparent,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Chip (emoji)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.8f),
                                    spotColor = Color.Black.copy(alpha = 0.8f)
                                )
                                .background(Color.White, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = emojis[index]),
                                contentDescription = descricoes[index],
                                modifier = Modifier.size(35.dp)
                            )
                        }

                        // Sempre reservar espaço abaixo (mesmo que esteja vazio)
                        if (index % 2 != 0) {
                            Text(
                                text = semana,
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            // Espaço invisível para manter alinhamento
                            Text(
                                text = stringResource(id = R.string.dashboard_func_calendar_week_x),
                                fontSize = 10.sp,
                                color = Color.Transparent,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Setas entre os chips
                    if (index < semanas.lastIndex) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.seta),
                            contentDescription = "Seta",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}
