package com.example.donacionesjava.domain

open class Administrador : Usuario {
    var idAdministrador: String? = null

    constructor(
        idAdministrador: String,
        idUsuario: String,
        nombre: String,
        email: String,
        contrasena: String
    ) : super(idUsuario, nombre, email, contrasena) {
        this.idAdministrador = idAdministrador
    }

    constructor(nombre: String, email: String, contrasena: String) : super(nombre, email, contrasena)


    override fun toString(): String {
        return "Administrador(idAdministrador=$idAdministrador) ${super.toString()}"
    }
}
