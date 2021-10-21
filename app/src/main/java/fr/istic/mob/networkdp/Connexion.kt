package fr.istic.mob.networkdp

import kotlinx.serialization.Serializable

@Serializable
class Connexion(private var debut: Node) {
    private lateinit var fin: Node

    constructor(debut: Node, fin: Node) : this(debut) {
        this.debut = debut
        this.fin = fin
    }

    /**
     * getters and setters for debut
     */
    fun getEmitter(): Node {
        return this.debut
    }

    /**
     * getters and setters for fin
     */
    fun getReceiver(): Node {
        return this.fin
    }
}