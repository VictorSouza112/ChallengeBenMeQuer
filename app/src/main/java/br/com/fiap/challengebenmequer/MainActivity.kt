package br.com.fiap.challengebenmequer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.fiap.challengebenmequer.navigation.AppNavigation
import br.com.fiap.challengebenmequer.ui.theme.ChallengeBenMeQuerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeBenMeQuerTheme {
                AppNavigation()
            }
        }
    }
}