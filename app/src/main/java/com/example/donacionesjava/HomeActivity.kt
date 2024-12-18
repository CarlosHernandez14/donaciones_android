package com.example.donacionesjava

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estado para los creadores de contenido
    var creadores by remember { mutableStateOf<List<CreadorContenido>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Efecto para cargar los datos iniciales
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.apiService.getCreadores()
            creadores = response.data
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error al cargar los datos: ${e.message}"
            isLoading = false
        }
    }

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
                // Text que muestra un mensaje de carga
                Text(text = "Cargando...", style = MaterialTheme.typography.bodyMedium)
            }
            errorMessage != null -> {
                Text(text = errorMessage!!, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }
            else -> {
                CreadoresContenidoList(
                    creadores = creadores,
                    onHacerPartnerClick = { updatedCreador ->
                        val updatedList = creadores.map {
                            if (it.idUsuario == updatedCreador.idUsuario) {
                                it.copy(partner = !it.partner)
                            } else it
                        }
                        creadores = updatedList

                        coroutineScope.launch {
                            try {
                                val response = RetrofitInstance.apiService.updateCreador(updatedCreador)
                                if (response.OK) {
                                    Toast.makeText(context, "Creador actualizado con éxito", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error al actualizar al usuario", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onVerSuscriptoresClick = { creador ->
                        Toast.makeText(context, "Ver suscriptores de ${creador.idUsuario}", Toast.LENGTH_SHORT).show()
                    }
                )
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
    val context = LocalContext.current
    var nombreUsuario by remember { mutableStateOf<String?>("Cargando...") }

    // Efecto para obtener el usuario asociado al creador
    LaunchedEffect(creador) {
        try {
            val usuarios = RetrofitInstance.apiService.getUsuarios().data
            val usuario = usuarios.find { it.idUsuario == creador.idUsuario }
            nombreUsuario = usuario?.nombre ?: "Desconocido"
        } catch (e: Exception) {
            nombreUsuario = "Error al cargar"
            Toast.makeText(context, "Error al cargar el usuario: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA726)),
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
                Text(
                    text = nombreUsuario ?: "Cargando...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
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
                    Text(text = if (creador.partner) "Deshacer partner" else "Hacer Partner", color = Color.White)
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

