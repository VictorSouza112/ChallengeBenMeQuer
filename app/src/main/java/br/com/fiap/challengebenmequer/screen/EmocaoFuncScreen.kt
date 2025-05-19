package br.com.fiap.challengebenmequer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.component.footer.MenuFooterFunc
import br.com.fiap.challengebenmequer.component.header.MenuHeaderFunc

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EmocaoFuncScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedEmoji by remember { mutableStateOf<String?>(null) }
    var selectedFeeling by remember { mutableStateOf<String?>(null) }
    var textoDoDia by remember { mutableStateOf("") }
    var enviarClicado by remember { mutableStateOf(false) }

    val labels = stringArrayResource(id = R.array.emotion_func_emotions)
    val sentimentos = stringArrayResource(id = R.array.emotion_func_feelings)

    val emocoes = listOf(
        Triple(labels[0], R.drawable.emoji_feliz, colorResource(id = R.color.verde_alegre)),
        Triple(labels[1], R.drawable.emoji_triste, colorResource(id = R.color.azul_triste)),
        Triple(labels[2], R.drawable.emoji_bravo, colorResource(id = R.color.vermelho_bravo)),
        Triple(labels[3], R.drawable.emoji_ansioso, colorResource(id = R.color.laranja_ansioso)),
        Triple(labels[4], R.drawable.emoji_cansado, colorResource(id = R.color.cinza_cansado)),
        Triple(labels[5], R.drawable.emoji_medo, colorResource(id = R.color.roxo_medo))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.cinza))
    ) {
        MenuHeaderFunc(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, bottom = 90.dp)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.emotion_func_title_emotion),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.azul_escuro)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        @Composable
                        fun EmojiChip(
                            label: String,
                            drawable: Int,
                            selected: Boolean,
                            onClick: () -> Unit,
                            textColor: Color,
                            selectedBackgroundColor: Color = colorResource(id = R.color.verde),
                            defaultBackgroundColor: Color = Color.LightGray
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .size(70.dp)
                                    .shadow(10.dp, RoundedCornerShape(10.dp))
                                    .border(2.dp, colorResource(id = R.color.cinza_cansado), RoundedCornerShape(10.dp))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selected) selectedBackgroundColor else defaultBackgroundColor)
                                    .clickable { onClick() }
                            ) {
                                Image(
                                    painter = painterResource(id = drawable),
                                    contentDescription = label,
                                    modifier = Modifier.size(55.dp)
                                )
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    color = textColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                        ) {
                            emocoes.take(3).forEach { (label, drawable, colorTexto) ->
                                EmojiChip(label, drawable, selectedEmoji == label, { selectedEmoji = label }, colorTexto)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                        ) {
                            emocoes.drop(3).forEach { (label, drawable, colorTexto) ->
                                EmojiChip(label, drawable, selectedEmoji == label, { selectedEmoji = label }, colorTexto)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(id = R.string.emotion_func_title_feeling),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.azul_escuro)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        var dropdownExpanded by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .border(2.dp, colorResource(id = R.color.azul), RoundedCornerShape(10.dp))
                                .shadow(8.dp, RoundedCornerShape(10.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(colorResource(id = R.color.verde))
                                    .clickable { dropdownExpanded = true }
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedFeeling ?: stringResource(id = R.string.emotion_func_dropdown_placeholder),
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false }
                            ) {
                                sentimentos.forEach { sentimento ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                sentimento,
                                                color = colorResource(id = R.color.azul_claro),
                                                fontSize = 14.sp
                                            )
                                        },
                                        onClick = {
                                            selectedFeeling = sentimento
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(id = R.string.emotion_func_title_day_description),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E2B4D)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = textoDoDia,
                            onValueChange = { textoDoDia = it },
                            placeholder = { Text(stringResource(id = R.string.emotion_func_placeholder_message), color = Color.White) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, colorResource(id = R.color.azul), RoundedCornerShape(10.dp))
                                .shadow(8.dp, RoundedCornerShape(10.dp))
                                .height(120.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = colorResource(id = R.color.verde),
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { navController.navigate("dashboardfunc") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .widthIn(min = 100.dp)
                                .height(45.dp)
                                .border(2.dp, colorResource(id = R.color.azul_escuro), RoundedCornerShape(10.dp))
                        ) {
                            Text(stringResource(id = R.string.emotion_func_button_back), color = colorResource(id = R.color.azul_escuro))
                        }

                        Button(
                            onClick = { enviarClicado = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (enviarClicado)
                                    colorResource(id = R.color.verde)
                                else
                                    colorResource(id = R.color.azul)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .widthIn(min = 100.dp)
                                .height(45.dp)
                                .border(2.dp, colorResource(id = R.color.azul_escuro), RoundedCornerShape(10.dp))
                        ) {
                            Text(stringResource(id = R.string.emotion_func_button_send), color = Color.White)
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            MenuFooterFunc(onNavigate = { route -> navController.navigate(route) })
        }
    }
}