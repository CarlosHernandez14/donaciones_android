package com.example.donacionesjava

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donacionesjava.components.InputCard
import com.example.donacionesjava.components.RoleSelector
import com.example.donacionesjava.domain.Usuario
import com.example.donacionesjava.ui.theme.DonacionesJavaTheme
import com.example.donacionesjava.ws_manager.RetrofitInstance
import kotlinx.coroutines.launch
import java.util.Date

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DonacionesJavaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterView()
                }
            }
        }
    }
}

@Composable
fun RegisterView() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD6EDF5)),
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
        RegisterForm()
    }
}

@Composable
fun RegisterForm() {

    val context = LocalContext.current;

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordRepeat by remember {
        mutableStateOf("")
    }

    var selectedRole by remember { mutableStateOf("Usuario") }

    val coroutineScope = rememberCoroutineScope();

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(Color.White, shape = RoundedCornerShape(15)),
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TITTLE
        Text(text = "Crear cuenta", fontSize = 35.sp, fontFamily = FontFamily.Serif, color = Color(0xFFC472C6))

        InputCard(cardName = "Nombre", inputValue = name, onValueChange = { name = it })
        // Email input
        InputCard(cardName = "Correo", email, onValueChange = {email = it})

        InputCard(cardName = "Contrasena", password, onValueChange = { password = it })

        // Aquí invocamos RoleSelector pasando el estado y el callback
        RoleSelector(
            selectedRole = selectedRole,
            onRoleSelected = { newRole -> selectedRole = newRole }
        )


        // BUtton to access
        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordRepeat.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        val usuario = Usuario(
                            nombre = name,
                            correo = email,
                            contrasena = password
                        )

                        coroutineScope.launch {
                            try {
                                // Hacer el POST con Retrofit
                                val response = RetrofitInstance.apiService.registerUsuario(usuario)

                                // Mostrar un mensaje según la respuesta del servidor
                                if (response.OK) {
                                    Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                                    // Redirigir al login o a donde necesites
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(5.dp)) // Agregar sombra
                .height(45.dp), // Altura del botón
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC472C6)
            ),
            shape = RoundedCornerShape(8.dp), // Redondeado del botón
        ) {
            Text(text = "Registrar", fontSize = 18.sp)
        }


    }
}