package fr.eseo.ld.android.cp.notes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.eseo.ld.android.cp.notes.R
import fr.eseo.ld.android.cp.notes.ui.theme.NoteTakerTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.eseo.ld.android.cp.notes.model.Note
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModel
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")        // Pour éviter l'erreur de paramètre inutilisé
@Composable
fun DetailsScreen(navController : NavController, noteId : String, viewModel : NoteTakerViewModel){

    val existingNote by viewModel.note.collectAsState()
    // Add the following lines, which will ensure that the existingNote instance is correctly initialized when the composable is (re)launched:
    LaunchedEffect(noteId){
        viewModel.getNoteById(noteId)
    }

    var title by remember {mutableStateOf(existingNote?.title ?: "")}
    var body by remember {mutableStateOf(existingNote?.body ?: "")}
    val author = "Bob"
    val date = Date()


    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = NoteTakerScreens.SUMMARY_SCREEN.id
    ) {
        composable(
            NoteTakerScreens.DETAILS_SCREEN.id + "/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                }
            )
        ) {
                backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "NEW"
            DetailsScreen(navController, noteId, viewModel)
        }
    }


    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ){
        Scaffold(
            topBar = {
                TopAppBar (
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    navigationIcon = {
                        IconButton(onClick = {navController.popBackStack()}){
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    },
                    actions = {
                        if(existingNote==null || existingNote!!.author == "Bob") {
                            IconButton(onClick = {
                                    val newNote = Note(
                                        creationDate = existingNote?.creationDate ?: date,
                                        id = existingNote?.id ?: System.currentTimeMillis().toString(),
                                        title = title,
                                        body = body,
                                        author = author,
                                        modificationDate = date
                                    )
                                    viewModel.addOrUpdate(newNote)
                                    navController.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    }
                )
            },
            content = {innerPadding -> DetailsContent(existingNote = null ,innerPadding = innerPadding) }

        )
    }
}

@Composable
private fun DetailsContent(existingNote : Note?, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                TextField(
                    value = existingNote!!.title,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge,
                    onValueChange = { existingNote.title = it},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Title") },
                    readOnly = if (existingNote == null || existingNote.author == "Bob") false else true
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = existingNote.body,
                    singleLine = false,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    onValueChange = { existingNote.body = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    label = { Text(text = "Body") },
                    readOnly = if (existingNote == null || existingNote.author == "Bob") false else true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Created By")
                    Text(text = existingNote.author ?: "")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Creation Date")
                    Text(text = formatDate(existingNote.creationDate))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Last modification Date")
                    Text(text = formatDate(existingNote.modificationDate ?: Date()))
                }
            }
        }
}

}


@SuppressLint("SimpleDateFormat")
private fun formatDate(date : Date) : String {
    return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date)
}




@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewNotesSummaryScreen(){
    NoteTakerTheme{
//        DetailsScreen()
    }
}
