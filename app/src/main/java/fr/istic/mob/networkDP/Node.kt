package fr.istic.mob.networkDP

import android.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
class Node(private var titre: String) {
    private var posX: Float = 0F
    private var posY: Float = 0F
    private var couleur: Int = Color.RED

    constructor(posx: Float, posy: Float, titre: String) : this(titre) {
        this.titre = titre
        this.posX = posx
        this.posY = posy
    }

    fun getcouleur(): Int {
        return this.couleur
    }

    fun setcouleur(i: Int) {
        this.couleur = i
    }

    fun getTitre(): String {
        return this.titre
    }

    fun setTitre(s: String) {
        this.titre = s
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (this.titre != other.titre) return false
        if (this.posX != other.posX) return false
        if (this.posY != other.posY) return false

        return true
    }

    override fun hashCode(): Int {
        var result = titre.hashCode()
        result = 31 * result + posX.hashCode()
        result = 31 * result + posY.hashCode()
        result = 31 * result + couleur
        return result
    }


}