package fr.eseo.ld.android.cp.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import fr.eseo.ld.android.cp.notes.hiltmodules.FirebaseModule
import fr.eseo.ld.android.cp.notes.repository.FirestoreRepository
import fr.eseo.ld.android.cp.notes.ui.NoteTakerApp
import fr.eseo.ld.android.cp.notes.ui.theme.NoteTakerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteTakerTheme {
                val repository = FirestoreRepository(FirebaseFirestore.getInstance())

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    NoteTakerApp(application, repository)
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    NoteTakerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
//            NoteTakerApp(Application(), FirestoreRepository(FirebaseFirestore.getInstance()))
        }
    }
}