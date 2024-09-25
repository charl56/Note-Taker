package fr.eseo.ld.android.cp.notes.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.eseo.ld.android.cp.filmposter.ui.view.SummaryScreen
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.ui.screens.DetailsScreen
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModel


@Composable
fun NoteTakerApp() {

    val viewModel = NoteTakerViewModel()
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = NoteTakerScreens.SUMMARY_SCREEN.id
    ) {
        composable(
            NoteTakerScreens.SUMMARY_SCREEN.id
        ) {
            SummaryScreen(navController, viewModel)
        }
    }
}