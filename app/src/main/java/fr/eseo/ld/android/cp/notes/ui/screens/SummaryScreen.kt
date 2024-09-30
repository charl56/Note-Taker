// HeaderScreen.kt
package fr.eseo.ld.android.cp.notes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.eseo.ld.android.cp.notes.model.Note
import fr.eseo.ld.android.cp.notes.ui.navigation.NoteTakerScreens
import fr.eseo.ld.android.cp.notes.viewmodels.NoteTakerViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")        // Pour éviter l'erreur de paramètre inutilisé
@Composable
fun SummaryScreen(navController : NavController, viewModel : NoteTakerViewModel){

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
                onClick = {navController.navigate(
                    NoteTakerScreens.DETAILS_SCREEN.id+"/${it}"
                )})
            }
        )
    }
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


@Composable
private fun SummaryItem(note : Note,  onClick : (String) -> Unit){
    Card(
        onClick = {onClick(note.id)},
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
fun SummaryList(notes : List<Note>, modifier : Modifier = Modifier, onClick : (String) -> Unit)
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
            SummaryItem(note, onClick = {onClick(it)})
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
        val viewModel: NoteTakerViewModel = viewModel()
        val navController: NavHostController = rememberNavController()
        SummaryScreen(navController, viewModel)
    }
}
