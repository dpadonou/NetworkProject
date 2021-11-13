package fr.istic.mob.networkdp

import android.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
class Connexion(private var debut: Node) {
    private lateinit var fin: Node
    private var etiquette: String = ""
    private var couleur: Int = Color.YELLOW
    private var epaisseur: Float = 20F
    var isCurved: Boolean = false
    var mX: Float = 0F
    var mY: Float = 0F
    private var middlePosition = FloatArray(2)

    constructor(debut: Node, fin: Node) : this(debut) {
        this.debut = debut
        this.fin = fin
        this.middlePosition[0] = (this.debut.getPosX() + this.fin.getPosX()) / 2
        this.middlePosition[1] = (this.debut.getPosY() + this.fin.getPosY()) / 2
    }

    /**
     * getters pour le noeud de depart de la connexion
     */
    fun getEmitter(): Node {
        return this.debut
    }

    /**
     * getters pour le noeud de fin de la connexion
     */
    fun getReceiver(): Node {
        return this.fin
    }

    /**
     * getters and setters pour l'etiquette de la connexion
     */
    fun getetiquette(): String {
        return this.etiquette
    }

    fun setetiquette(s: String) {
        this.etiquette = s
    }

    /** getters et setters pour la couleur **/
    fun getcouleur(): Int {
        return this.couleur
    }

    fun setcouleur(i: Int) {
        this.couleur = i
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
        return this.middlePosition
    }
    /**Fournit des positions au milieu **/
    fun setMiddle(x:Float, y:Float) {
        this.middlePosition[0] =x
        this.middlePosition[1] =y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Connexion

        if (this.debut != other.debut) return false
        if (this.fin != other.fin) return false
        if (this.etiquette != other.etiquette) return false

        return true
    }

    override fun hashCode(): Int {
        var result = debut.hashCode()
        result = 31 * result + fin.hashCode()
        result = 31 * result + etiquette.hashCode()
        result = 31 * result + couleur
        result = 31 * result + epaisseur.hashCode()
        result = 31 * result + isCurved.hashCode()
        result = 31 * result + mX.hashCode()
        result = 31 * result + mY.hashCode()
        return result
    }


}