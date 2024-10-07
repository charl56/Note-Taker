package fr.eseo.ld.android.cp.notes.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.eseo.ld.android.cp.notes.repository.FirestoreRepository
import fr.eseo.ld.android.cp.notes.ui.screens.SummaryScreen
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.ui.screens.ConnectionScreen
import fr.eseo.ld.android.cp.notes.ui.screens.DetailsScreen
import fr.eseo.ld.android.cp.notes.viewmodels.AuthenticationViewModel


@Composable
fun NoteTakerApp(application : Application, repository: FirestoreRepository) {
    val navController: NavHostController = rememberNavController()
    val authenticationViewModel : AuthenticationViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "start") {

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
            SummaryScreen(navController, application, repository, authenticationViewModel)
        }
        composable(NoteTakerScreens.DETAILS_SCREEN.id + "/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "NEW"
            DetailsScreen(navController, noteId, application, repository, authenticationViewModel)
        }
        composable(NoteTakerScreens.CONNECTION_SCREEN.id) {
            ConnectionScreen(navController, authenticationViewModel)
        }
    }
}
