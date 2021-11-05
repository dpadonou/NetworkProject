package fr.istic.mob.networkdp

import android.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
class Node(private var titre: String) {
    private var posX: Float = 0F
    private var posY: Float = 0F
    private var couleur:Int = Color.RED

    constructor(posx: Float, posy: Float, titre: String) : this(titre) {
        this.titre = titre
        this.posX = posx
        this.posY = posy
    }
    fun getcouleur():Int
    {
        return this.couleur
    }
    fun setcouleur(i:Int)
    {
       this.couleur = i
    }
    fun getTitre(): String {
        return this.titre
    }
    fun setTitre(s:String) {
        this.titre =s
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