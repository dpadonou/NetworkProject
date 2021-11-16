package fr.istic.mob.networkdp

import android.graphics.Color
import android.graphics.Path
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import android.graphics.PathMeasure
import kotlin.io.path.Path


@Serializable
class Connexion(private var debut: Node) {
    private lateinit var fin: Node
    private var etiquette: String = ""
    private var couleur: Int = Color.YELLOW
    private var epaisseur: Float = 20F
    private var middlePosition = FloatArray(2)
    var isCurved: Boolean = false
    var mX: Float = 0F
    var mY: Float = 0F
    private var myPath:ConnexionPath = ConnexionPath()

    constructor(debut: Node, fin: Node) : this(debut) {
        this.debut = debut
        this.fin = fin
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
    /** getters du path de la connexion **/
    fun getPath():Path{
        myPath = ConnexionPath()
        if(isCurved){
            myPath.moveTo(this.debut.getPosX(), this.debut.getPosX())
            myPath.quadTo(
                (middlePosition[0] + mX) / 2,
                (middlePosition[1] + mY) / 2,
                fin.getPosX(),
                fin.getPosY()
            )

        }else{
            myPath.moveTo(this.debut.getPosX(), this.debut.getPosY())
            myPath.lineTo(this.fin.getPosX(),this.fin.getPosY())
        }
        calculMiddle(myPath)
        return myPath
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

    private fun calculMiddle(p:Path){
        val pm = PathMeasure(myPath, false)
        //get coordinates of the middle point
        pm.getPosTan(pm.length * 0.5f, middlePosition, null)
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