package com.example.donacionesjava

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.donacionesjava.components.InputCard
import com.example.donacionesjava.dao.WSManager
import com.example.donacionesjava.domain.Usuario
import com.example.donacionesjava.ui.theme.DonacionesJavaTheme
import com.example.donacionesjava.ws_manager.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DonacionesJavaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginView()
                }
            }
        }
    }
}

@Composable
fun LoginView() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff08b5c)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_insta),
            contentDescription = "logo bebe",
            modifier = Modifier
                .size(200.dp)  // Cambia el tamaño del logo
                .padding(top = 25.dp),  // Añade un margen superior
            contentScale = ContentScale.Fit
        )

        LoginForm()
    }
}

@Composable
fun LoginForm() {

    //val gtDao = MainApplication.gtDatabase.getGTDao();
    val coroutineScope = rememberCoroutineScope();


    val context = LocalContext.current;
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // We get all users

//    // Observa el LiveData de los usuarios y lo convierte a un estado composable
//    val users: LiveData<List<Usuario>> = gtDao.getAllUsers()
//    val usersList by users.observeAsState(initial = emptyList())

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(Color.White, shape = RoundedCornerShape(15)),
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TITTLE
        Text(text = "Inicia sesion", fontSize = 35.sp, fontFamily = FontFamily.Serif, color = Color(0xff9c3742))

        // Email input
        InputCard(cardName = "Correo", email, onValueChange = {email = it})

        InputCard(cardName = "Contrasena", password, onValueChange = { password = it })

        //Buttons for register and forgot password
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ClickableText(
                text = AnnotatedString("No tienes una cuenta? Registrate"),
                modifier = Modifier.padding(8.dp), // Agregar un padding si lo deseas
                style = TextStyle(
                    color = Color(0xff9c3742), // Color en hexadecimal (morado en este ejemplo)
                    fontSize = 13.sp, // Tamaño de la fuente
                    fontFamily = FontFamily.Serif // Tipo de fuente (puedes cambiarla)
                ),
                onClick = {
                    // Start the register activity
                    val intent = Intent(context, SignUpActivity::class.java);
                    context.startActivity(intent);
                }
            )

        }

        // BUtton to access
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        // Obtener todos los usuarios
                        val usuarios = RetrofitInstance.apiService.getUsuarios().data
                        // IMPRIMIR USUAIROS
                        usuarios.forEach {
                            Log.d("USUARIO", it.toString())
                        }
                        val usuarioEncontrado = usuarios.find { it.correo == email && it.contrasena == WSManager.hashSHA1(password) }

                        if (usuarioEncontrado != null) {
                            // Verificar si es administrador o creador
                            val admins = RetrofitInstance.apiService.getAdmins().data
                            val creadores = RetrofitInstance.apiService.getCreadores().data

                            val esAdmin = admins.any { it.idUsuario == usuarioEncontrado.idUsuario }
                            val esCreador = creadores.any { it.idUsuario == usuarioEncontrado.idUsuario }

                            // Mostrar mensajes según el tipo de usuario
                            when {
                                esAdmin -> {
                                    Toast.makeText(context, "Bienvenido, administrador", Toast.LENGTH_SHORT).show()

                                    // Redirigir a la actividad de administrador
                                    val intent = Intent(context, HomeActivity::class.java)
                                    context.startActivity(intent)
                                }
                                esCreador -> {
                                    Toast.makeText(context, "Bienvenido, creador", Toast.LENGTH_SHORT).show()
                                    // Redirigir a la actividad de creador
                                    // Mensaje de que necesita ser administrador para acceder
                                    Toast.makeText(context, "Necesitas ser administrador para acceder", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(context, "Bienvenido, usuario", Toast.LENGTH_SHORT).show()
                                    // Redirigir a la actividad de usuario
                                    // Mensaje de que necesita ser administrador para acceder
                                    Toast.makeText(context, "Necesitas ser administrador para acceder", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Error", e.message.toString())
                        e.printStackTrace()
                    }
                }
            },
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(5.dp)) // Agregar sombra
                .height(45.dp), // Altura del botón
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff9c3742)
            ),
            shape = RoundedCornerShape(8.dp), // Redondeado del botón
        ) {
            Text(text = "Acceso", fontSize = 18.sp)
        }


    }
}