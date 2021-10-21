package fr.istic.mob.networkdp

import kotlinx.serialization.Serializable

@Serializable
class Node(private var titre: String) {
    private var posX: Float = 0F
    private var posY: Float = 0F

    constructor(posx: Float, posy: Float, titre: String) : this(titre) {
        this.titre = titre
        this.posX = posx
        this.posY = posy
    }

    fun getTitre(): String {
        return this.titre
    }

    fun getPosX(): Float {
        return this.posX
    }

    fun getPosY(): Float {
        return this.posY
    }

    fun setPosX(x: Float) {
        this.posX = x
    }

    fun setPosY(y: Float) {
        this.posY = y
    }
}