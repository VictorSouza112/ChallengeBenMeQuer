package br.com.fiap.challengebenmequer.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // Para Toasts, se necessário
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Para instanciar ViewModel
import androidx.navigation.NavController
import br.com.fiap.challengebenmequer.R
import br.com.fiap.challengebenmequer.data.repository.SessionRepository
import br.com.fiap.challengebenmequer.viewmodel.AuthViewModel // Importe seu AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel() // Instancia o AuthViewModel
) {
    // Estados para os campos de texto
    var usernameInput by remember { mutableStateOf("TigreBranco123") } // Pré-preenche para teste
    var passwordInput by remember { mutableStateOf("senha_mock_123") } // Pré-preenche para teste

    // Observa estados do ViewModel
    val isLoading by authViewModel.isLoading
    val apiResponseMessage by authViewModel.apiResponseMessage
    val currentToken by SessionRepository.authToken

    // Efeito para navegar quando o login for bem-sucedido (token recebido)
    LaunchedEffect(currentToken) {
        if (currentToken != null) {
            Log.d("LoginScreen", "Token encontrado no SessionRepository: $currentToken. Navegando para bemvindofunc.")
            navController.navigate("bemvindofunc") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login BenMeQuer", // Título da tela
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = usernameInput,
                onValueChange = { usernameInput = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
            }

            apiResponseMessage?.let {
                Text(
                    text = it,
                    color = if (it.contains("Falha") || it.contains("Erro")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    Log.d("LoginScreen", "Tentando login com: $usernameInput")
                    // Limpa mensagem anterior antes de nova tentativa
                    authViewModel.clearApiResponseMessage() // Função a ser criada no ViewModel
                    authViewModel.loginUser(usernameInput, passwordInput)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !isLoading
            ) {
                Text(stringResource(id = R.string.login_enter))
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Texto clicável para cadastro
            Text(
                text = stringResource(id = R.string.login_register),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.azul),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("cadastro")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.login_test), // "Testar Tela Empresa"
                fontSize = 14.sp,
                color = colorResource(id = R.color.azul),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("emocaoempre")
                }
            )
        }
    }
}