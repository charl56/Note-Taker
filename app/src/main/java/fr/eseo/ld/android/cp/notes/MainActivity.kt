package fr.eseo.ld.android.cp.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import fr.eseo.ld.android.cp.filmposter.ui.view.NotesSummaryAppBar
import fr.eseo.ld.android.cp.notes.ui.theme.NoteTakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteTakerTheme {
                NotesSummaryAppBar(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    NoteTakerTheme{
        NotesSummaryAppBar(
            modifier = Modifier.fillMaxSize(),
        )
    }
}