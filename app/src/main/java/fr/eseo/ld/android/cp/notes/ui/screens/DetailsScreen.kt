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
import androidx.compose.ui.unit.dp
import fr.eseo.ld.android.cp.notes.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.eseo.ld.android.cp.notes.model.Note
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModel
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")        // Pour éviter l'erreur de paramètre inutilisé
@Composable
fun DetailsScreen(
    navController : NavController,
    noteId : String,
    viewModel : NoteTakerViewModel = viewModel()
) {

    // Add the following lines, which will ensure that the existingNote instance is correctly initialized when the composable is (re)launched:
    LaunchedEffect(noteId){
        viewModel.getNoteById(noteId)
    }

    val existingNote by viewModel.note.collectAsState()

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    val author = "Bob"
    val date = Date()

    SideEffect {
        title = existingNote?.title ?: ""
        body = existingNote?.body ?: ""
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
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                        )
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
            content = {innerPadding -> DetailsContent(
                existingNote = existingNote,
                innerPadding = innerPadding,
                title = title,
                body = body,
                onTitleChange = { title = it },
                onBodyChange = { body = it }
                ) }
        )
    }
}

@Composable
private fun DetailsContent(
    existingNote : Note?,
    innerPadding: PaddingValues,
    title: String,
    body: String,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit
) {
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
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp)
            ) {
                TextField(
                    value = title,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge,
                    onValueChange = onTitleChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Title") },
                    readOnly = !(existingNote == null || existingNote.author == "Bob"),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Transparent,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = body,
                    singleLine = false,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    onValueChange = onBodyChange,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    label = { Text(text = "Body") },
                    readOnly = !(existingNote == null || existingNote.author == "Bob"),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Transparent,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Created By")
                    Text(text = existingNote?.author ?: "")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Creation Date")
                    Text(text = formatDate(existingNote?.creationDate ?: Date()))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "Last modification Date")
                    Text(text =  formatDate(existingNote?.modificationDate ?: Date()))
                }
            }
        }
}

}


@SuppressLint("SimpleDateFormat")
private fun formatDate(date : Date) : String {
    return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date)
}
