package fr.istic.mob.networkDP

import android.graphics.Color
import android.graphics.Path
import android.graphics.PathMeasure
import kotlinx.serialization.Serializable


@Serializable
class Connexion(private var startNode: Node) {
    private lateinit var endNode: Node
    private var tag: String = ""
    private var color: Int = Color.YELLOW
    private var thickness: Float = 20F
    private var middlePosition = FloatArray(2)
    var isCurved: Boolean = false
    var mX: Float = 0F
    var mY: Float = 0F
    private var myPath:ConnexionPath = ConnexionPath()

    constructor(debut: Node, fin: Node) : this(debut) {
        this.startNode = debut
        this.endNode = fin
    }

    /**
     * getters et setters pour le noeud de depart de la connexion
     */
    fun getEmitter(): Node {
        return this.startNode
    }
    fun setEmitter(node:Node) {
        this.startNode = node
    }

    /**
     * getters et setters pour le noeud de fin de la connexion
     */
    fun getReceiver(): Node {
        return this.endNode
    }
    fun setReceiver(node:Node) {
        this.endNode = node
    }

    /**
     * getters et setters pour l'etiquette de la connexion
     */
    fun getTag(): String {
        return this.tag
    }

    fun setTag(s: String) {
        this.tag = s
    }

    /** getters et setters pour la couleur **/
    fun getColor(): Int {
        return this.color
    }

    fun setColor(i: Int) {
        this.color = i
    }


    /** getters et setters pour l'epaisseur **/
    fun getThickness(): Float {
        return this.thickness
    }

    fun setThickness(f: Float) {
        this.thickness = f
    }
    /** getters du path de la connexion **/
    fun getPath():Path{
        myPath = ConnexionPath()
        if(isCurved){
            myPath.moveTo(this.startNode.getPosX(), this.startNode.getPosY())
            myPath.quadTo(
                (middlePosition[0] + mX) / 2,
                (middlePosition[1] + mY) / 2,
                endNode.getPosX(),
                endNode.getPosY()
            )

        }else{
            myPath.moveTo(this.startNode.getPosX(), this.startNode.getPosY())
            myPath.lineTo(this.endNode.getPosX(),this.endNode.getPosY())
        }
        calculMiddle()
        return myPath
    }

    /**Retourne la position du milieu **/
    fun getMiddle(): FloatArray {
        return this.middlePosition
    }

    private fun calculMiddle(){
        val pm = PathMeasure(myPath, false)
        pm.getPosTan(pm.length * 0.5f, middlePosition, null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Connexion

        if (this.startNode != other.startNode) return false
        if (this.endNode != other.endNode) return false
        if (this.tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startNode.hashCode()
        result = 31 * result + endNode.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + color
        result = 31 * result + thickness.hashCode()
        result = 31 * result + isCurved.hashCode()
        result = 31 * result + mX.hashCode()
        result = 31 * result + mY.hashCode()
        return result
    }


}