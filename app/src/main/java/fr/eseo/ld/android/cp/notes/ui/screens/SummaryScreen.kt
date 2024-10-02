// HeaderScreen.kt
package fr.eseo.ld.android.cp.notes.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.eseo.ld.android.cp.notes.R
import fr.eseo.ld.android.cp.notes.ui.theme.NoteTakerTheme
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.eseo.ld.android.cp.notes.model.Note
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModel
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModelFactory


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")        // Pour éviter l'erreur de paramètre inutilisé
@Composable
fun SummaryScreen(navController : NavController, application: Application){

    val viewModel = NoteTakerViewModelFactory(application)
        .create(NoteTakerViewModel::class.java)

    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    if(noteToDelete != null) {
        ConfirmDeleteDialog(note = noteToDelete!!,
            onConfirm = {
                viewModel.deleteNote(noteToDelete!!.id)
                noteToDelete = null
            },
            onDismiss = {
                noteToDelete = null
            }
        )
    }


            val notes by viewModel.notes.collectAsState()

    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ){
        Scaffold(
            topBar = {
                val layoutDirection = LocalLayoutDirection.current

                SimpleComposeAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start= WindowInsets.safeDrawing
                                .asPaddingValues()
                                .calculateStartPadding(layoutDirection),
                            end = WindowInsets.safeDrawing
                                .asPaddingValues()
                                .calculateEndPadding(layoutDirection)
                        )
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate(NoteTakerScreens.DETAILS_SCREEN.id+"/NEW")
                },
                   containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(imageVector = Icons.Default.Add,
                        contentDescription = R.string.add_note.toString(),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            content = {innerPadding ->
                SummaryList(
                    notes = notes,
                    modifier = Modifier.padding(innerPadding),
                    onClick = {navController.navigate(NoteTakerScreens.DETAILS_SCREEN.id+"/${it}") },
                    onLongClick = {it ->noteToDelete = notes.find{note -> note.id == it} }
                )
            }
        )
    }
}

@Composable
fun ConfirmDeleteDialog(note: Note, onConfirm: () -> Unit, onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Delete Note")
        },
        text = {
            Text(text = "Are you sure you wish to delete ${note.title}")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "No")
            }
        }
    )

}


@Composable
private fun SimpleComposeAppBar(modifier : Modifier = Modifier){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(
            text = stringResource(id= R.string.app_name),
            modifier = Modifier.padding(start=16.dp, top = 8.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,

        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SummaryItem(note : Note,  onClick : (String) -> Unit, onLongClick : (String) -> Unit){
    Card(
        modifier = Modifier.combinedClickable (
            onClick = {onClick(note.id)},
            onLongClick = {onLongClick(note.id)}
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.Black,
            containerColor = Color.White,
        ),
    ){
        Text(
            text = note.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = note.body,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .heightIn(max = 100.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}


@Composable
fun SummaryList(notes : List<Note>, modifier : Modifier = Modifier, onClick : (String) -> Unit, onLongClick : (String) -> Unit)
{
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.padding(8.dp).fillMaxHeight(1f),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 40.dp)
    ){
        items(notes){
                note ->
            SummaryItem(note, onClick = {onClick(it)}, onLongClick = {onLongClick(it)})
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewNotesSummaryScreen(){
    NoteTakerTheme{
        val application = Application()
        val navController: NavHostController = rememberNavController()
        SummaryScreen(navController, application)
    }
}
