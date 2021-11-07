package fr.istic.mob.networkdp

import android.graphics.Color
import android.graphics.Paint
import kotlinx.serialization.Serializable

@Serializable
class Connexion(private var debut: Node) {
    private lateinit var fin: Node
    private var etiquette:String = ""
    private var couleur:Int = Color.YELLOW
    private var epaisseur:Float = 20F
    var isCurved:Boolean = false
    var mx:Float = 0F
    var my:Float = 0F

    constructor(debut: Node, fin: Node) : this(debut) {
        this.debut = debut
        this.fin = fin
    }

    /**
     * getters for debut
     */
    fun getEmitter(): Node {
        return this.debut
    }

    /**
     * getters  for fin
     */
    fun getReceiver(): Node {
        return this.fin
    }

    /**
     * getters and setters for fin
     */
    fun getetiquette(): String {
        return this.etiquette
    }
    fun setetiquette(s:String) {
        this.etiquette = s
    }

    /** getters et setters pour la couleur **/
    fun getcouleur(): Int {
        return this.couleur
    }
    fun setcouleur(i:Int) {
        this.couleur=i
    }


    /** getters et setters pour l'epaisseur **/
    fun getEpaisseur(): Float {
        return this.epaisseur
    }
    fun setEpaisseur(f:Float) {
        this.epaisseur=f
    }

    /**Retourne la position du milieu **/
    fun getMiddle():FloatArray{
        val c = FloatArray(2)
          c[0] = (this.debut.getPosX() + this.fin.getPosX())/2
          c[1] = (this.debut.getPosY() + this.fin.getPosY())/2
        return c
    }
}