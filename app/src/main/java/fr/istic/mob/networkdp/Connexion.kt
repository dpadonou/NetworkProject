package fr.istic.mob.networkdp

import android.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
class Connexion(private var debut: Node) {
    private lateinit var fin: Node
    private var etiquette: String = ""
    private var color: Int = Color.YELLOW
    private var epaisseur: Float = 20F

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
    fun getEtiquette(): String {
        return this.etiquette
    }

    fun setEtiquette(s: String) {
        this.etiquette = s
    }

    /** getters et setters pour la couleur **/
    fun getColor(): Int {
        return this.color
    }

    fun setColor(i: Int) {
        this.color = i
    }

    /** getters et setters pour l'epaisseur **/
    fun getEpaisseur(): Float {
        return this.epaisseur
    }

    fun setEpaisseur(f: Float) {
        this.epaisseur = f
    }

    /**Retourne la position du milieu **/
    fun getMiddle(): FloatArray {
        val c = FloatArray(2)
        c[0] = (this.debut.getPosX() + this.fin.getPosX()) / 2
        c[1] = (this.debut.getPosY() + this.fin.getPosY()) / 2
        return c
    }
}