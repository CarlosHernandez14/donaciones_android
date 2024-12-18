package com.example.donacionesjava.domain

data class CreadorContenido(
    var idCreador: String? = null,
    var cuenta_bloqueada: Boolean = false,
    var partner: Boolean = false,
    var idUsuario: String? = null
)