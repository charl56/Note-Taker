package fr.eseo.ld.android.cp.notes.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.eseo.ld.android.cp.notes.ui.screens.SummaryScreen
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.ui.screens.DetailsScreen


@Composable
fun NoteTakerApp(application : Application) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = NoteTakerScreens.SUMMARY_SCREEN.id) {
        composable(NoteTakerScreens.SUMMARY_SCREEN.id) {
            SummaryScreen(navController, application)
        }
        composable(NoteTakerScreens.DETAILS_SCREEN.id + "/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "NEW"
            DetailsScreen(navController, noteId, application)
        }
    }
}
