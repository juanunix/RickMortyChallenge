package com.example.rickmortychallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.rickmortychallenge.theme.RickMortyChallengeTheme
import com.example.rickmortychallenge.ui.detail.CharacterDetailRoot
import com.example.rickmortychallenge.ui.screens.CharacterDetailRoute
import com.example.rickmortychallenge.ui.screens.CharacterListRoot
import com.example.rickmortychallenge.ui.screens.CharacterListRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            RickMortyChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CharacterListRoute
    ) {
        composable<CharacterListRoute> {
            CharacterListRoot(
                onCharacterSelected = { id ->
                    navController.navigate(CharacterDetailRoute(id))
                }
            )
        }
        composable<CharacterDetailRoute> { backStackEntry ->
            val route: CharacterDetailRoute = backStackEntry.toRoute()
            CharacterDetailRoot(
                characterId = route.characterId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
