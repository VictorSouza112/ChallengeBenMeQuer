package br.com.fiap.challengebenmequer.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import br.com.fiap.challengebenmequer.screen.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = "login",
        exitTransition = {
            slideOutOfContainer(
                towards =
                AnimatedContentScope.SlideDirection.End,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        enterTransition = {
            slideIntoContainer(
                towards =
                AnimatedContentScope.SlideDirection.Start,
                animationSpec = tween(1000)
            )
        }
    ) {
        composable(route = "login") {
            LoginScreen(navController)
        }
        composable(route = "cadastro") {
            CadastroScreen(navController)
        }
        composable(route = "bemvindofunc") {
            BemVindoFuncScreen(navController)
        }
        composable(route = "dashboardfunc") {
            DashboardFuncScreen(navController)
        }
        composable(route = "questionariofunc") {
            QuestionarioFuncScreen(navController)
        }
        composable(route = "emocaofunc") {
            EmocaoFuncScreen(navController)
        }
        composable(route = "calendariofunc") {
            CalendarioFuncScreen(navController)
        }
        composable(route = "emocaoempre") {
            EmocaoEmpreScreen(navController)
        }
        composable(route = "questionarioempre") {
            QuestionarioEmpreScreen(navController)
        }
        composable(route = "estrelaempre") {
            EstrelaEmpreScreen(navController)
        }
    }
}

