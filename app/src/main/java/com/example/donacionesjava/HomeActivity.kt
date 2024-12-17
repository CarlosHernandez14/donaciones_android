package com.example.donacionesjava

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donacionesjava.domain.CreadorContenido
import com.example.donacionesjava.ui.theme.DonacionesJavaTheme
import com.example.donacionesjava.ws_manager.ApiService
import com.example.donacionesjava.ws_manager.RetrofitInstance
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DonacionesJavaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeView();
                }
            }
        }
    }
}

@Composable
fun HomeView() {
    // Estado para los creadores de contenido
    var creadores by remember { mutableStateOf<List<CreadorContenido>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Efecto para cargar los datos una vez
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.apiService.getCreadores()
            creadores = response.data // Ajusta según cómo estructuraste tu `ApiResponse`
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error al cargar los datos: ${e.message}"
            isLoading = false
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFB0D0E7),
                        Color(0xFFD6EDF5),
                        Color(0xFFE1F5DA),
                        Color(0xFFCEE9BE)
                    )
                )
            ),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Welcome to the Home page", style = MaterialTheme.typography.labelLarge)

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Text(text = "Cargando...", style = MaterialTheme.typography.bodyMedium)
            }
            errorMessage != null -> {
                Text(text = errorMessage!!, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }
            else -> {
                LazyColumn {
                    items(creadores) { creador ->
                        CreadorContenidoCard(
                            creador = creador,
                            onHacerPartnerClick = { /* Lógica para hacer Partner */ },
                            onVerSuscriptoresClick = { /* Lógica para ver suscriptores */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreadorContenidoCard(
    creador: CreadorContenido,
    onHacerPartnerClick: (CreadorContenido) -> Unit,
    onVerSuscriptoresClick: (CreadorContenido) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA726)), // Anaranjado elegante
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(0xFFF57C00),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                creador.nombre?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
                Text(
                    text = if (creador.partner) "Partner" else "No es Partner",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Column {
                Button(
                    onClick = { onHacerPartnerClick(creador) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB74D))
                ) {
                    Text(text = "Hacer Partner", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onVerSuscriptoresClick(creador) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB74D))
                ) {
                    Text(text = "Suscriptores", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CreadoresContenidoList(
    creadores: List<CreadorContenido>,
    onHacerPartnerClick: (CreadorContenido) -> Unit,
    onVerSuscriptoresClick: (CreadorContenido) -> Unit
) {
    LazyColumn {
        items(creadores) { creador ->
            CreadorContenidoCard(
                creador = creador,
                onHacerPartnerClick = onHacerPartnerClick,
                onVerSuscriptoresClick = onVerSuscriptoresClick
            )
        }
    }
}