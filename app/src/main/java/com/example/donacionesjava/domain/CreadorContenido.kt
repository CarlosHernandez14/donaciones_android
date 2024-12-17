package com.example.donacionesjava.domain

open class CreadorContenido : Usuario {

    var idCreador: String? = null
    var cuentaBloqueada: Boolean = false
    var suscriptores: ArrayList<String> = ArrayList()
    var partner: Boolean = false

    constructor(
        idCreador: String,
        idUsuario: String,
        nombre: String,
        email: String,
        contrasena: String
    ) : super(idUsuario, nombre, email, contrasena) {
        this.idCreador = idCreador
    }

    constructor(
        idCreador: String,
        idUsuario: String,
        nombre: String,
        email: String,
        contrasena: String,
        suscriptores: ArrayList<String>
    ) : super(idUsuario, nombre, email, contrasena) {
        this.idCreador = idCreador
        this.suscriptores = suscriptores
    }

    constructor(nombre: String, email: String, contrasena: String) : super(nombre, email, contrasena)

    constructor(idCreador: String, cuentaBloqueada: Boolean, partner: Boolean) {
        this.idCreador = idCreador
        this.cuentaBloqueada = cuentaBloqueada
        this.partner = partner
    }



    override fun toString(): String {
        return "CreadorContenido(cuentaBloqueada=$cuentaBloqueada, partner=$partner, suscriptores=$suscriptores) ${super.toString()}"
    }
}