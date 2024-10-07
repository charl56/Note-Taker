// HeaderScreen.kt
package fr.eseo.ld.android.cp.notes.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.eseo.ld.android.cp.notes.R
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.eseo.ld.android.cp.notes.model.Note
import fr.eseo.ld.android.cp.notes.repository.FirestoreRepository
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.viewmodels.AuthenticationViewModel
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")        // Pour éviter l'erreur de paramètre inutilisé
@Composable
fun SummaryScreen(
    navController : NavController,
    viewModel: NoteTakerViewModel = viewModel(),
    authenticationViewModel: AuthenticationViewModel
) {

    val user by authenticationViewModel.user.observeAsState()
    var userConnected by remember{mutableStateOf(false)}
    val context = LocalContext.current
    LaunchedEffect(user) {
        userConnected = user?.isAnonymous?.not() ?: false
    }


    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    val notes by viewModel.notes.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.getNotes()
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


    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    actions = {
                        if(userConnected) {
                            Button(
                                onClick = {authenticationViewModel.logout()}
                            ){
                                Text(text="Log out")
                            }
                        } else {
                            Button(
                                onClick = {navController
                                    .navigate(NoteTakerScreens.CONNECTION_SCREEN.id)}
                            ){
                                Text(text="Connect")
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    if(userConnected) {
                        navController.navigate(NoteTakerScreens.DETAILS_SCREEN.id+"/NEW")
                    } else {
                        val toast = Toast.makeText(context, "You need to be login", Toast.LENGTH_SHORT)
                        toast.show()
                    }
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
