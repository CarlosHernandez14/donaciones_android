package com.example.donacionesjava.domain

open class CreadorContenido {

    var idCreador: String? = null
    var cuenta_bloqueada: Boolean = false
    var partner: Boolean = false
    var idUsuario: String? = null

    constructor()

    constructor(idCreador: String, cuenta_bloqueada: Boolean, partner: Boolean, idUsuario: String) {
        this.idCreador = idCreador
        this.cuenta_bloqueada = cuenta_bloqueada
        this.partner = partner
        this.idUsuario = idUsuario
    }

    // Con

    constructor(idCreador: String, partner: Boolean) {
        this.idCreador = idCreador
        this.partner = partner
    }



    override fun toString(): String {
        return "CreadorContenido(idCreador=$idCreador, cuenta_bloqueada=$cuenta_bloqueada, partner=$partner, idUsuario=$idUsuario)"
    }
}