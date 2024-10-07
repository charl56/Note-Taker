package fr.eseo.ld.android.cp.notes.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.eseo.ld.android.cp.notes.ui.screens.SummaryScreen
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.ui.screens.ConnectionScreen
import fr.eseo.ld.android.cp.notes.ui.screens.DetailsScreen
import fr.eseo.ld.android.cp.notes.viewmodels.AuthenticationViewModel
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModel


@Composable
fun NoteTakerApp() {
    val navController: NavHostController = rememberNavController()
    val viewModel : NoteTakerViewModel = hiltViewModel()
    val authenticationViewModel : AuthenticationViewModel = hiltViewModel()

    NavHost(navController , startDestination = "start") {

        composable("start") {
            val user by authenticationViewModel.user.observeAsState()
            LaunchedEffect(user) {
                if(user == null) {
                    authenticationViewModel.loginAnonymously()
                } else {
                    navController.navigate(NoteTakerScreens.SUMMARY_SCREEN.id) {
                        popUpTo("start"){inclusive = true}
                    }
                }
            }
        }
        composable(NoteTakerScreens.SUMMARY_SCREEN.id) {
            SummaryScreen(navController, viewModel, authenticationViewModel)
        }
        composable(NoteTakerScreens.DETAILS_SCREEN.id + "/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "NEW"
            DetailsScreen(navController, noteId, viewModel, authenticationViewModel)
        }
        composable(NoteTakerScreens.CONNECTION_SCREEN.id) {
            ConnectionScreen(navController, authenticationViewModel)
        }
    }
}
