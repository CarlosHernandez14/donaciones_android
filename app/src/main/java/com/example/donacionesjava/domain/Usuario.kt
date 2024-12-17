package com.example.donacionesjava.domain

import java.io.Serializable
import kotlin.random.Random

open class Usuario : Serializable {
    var id: String? = null
    var nombre: String? = null
    var correo: String? = null
    var contrasena: String? = null

    constructor()

    constructor(id: String, nombre: String, correo: String, contrasena: String) {
        this.id = id
        this.nombre = nombre
        this.correo = correo
        this.contrasena = contrasena
    }

    constructor(nombre: String, correo: String, contrasena: String) {
        this.nombre = nombre
        this.correo = correo
        this.contrasena = contrasena
    }

    companion object {
        fun generarId(): String {
            val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..10)
                .map { caracteres.random() }
                .joinToString("")
        }
    }

    override fun toString(): String {
        return "Usuario(id=$id, nombre=$nombre, correo=$correo, contrasena=$contrasena)"
    }
}