package fr.istic.mob.networkdp

import android.util.Log
import kotlinx.serialization.Serializable

@Serializable
class Graph {
    private  var titre:String = ""
    private val nodes: ArrayList<Node> = ArrayList()
    private val connexions: ArrayList<Connexion> = ArrayList()
    private var tmpConnexion:Connexion? = null
    private var selectedConnexion:Connexion? = null

   /* constructor(titre: String) {
        this.titre = titre
    }*/
    /** Methode pour recuperer le titre **/
    fun getTitre() : String{
        return this.titre
    }
    /** Methode pour attribuer une connexion temporaire **/
    fun settmpConnexion(c:Connexion?){
        this.tmpConnexion = c
    }
    /** Methode pour recuperer la connexion temporaire **/
    fun gettmpConnexion() : Connexion?{
        return this.tmpConnexion
    }
    /** Methode pour attribuer une connexion selectionnée **/
    fun setselectedConnexion(c:Connexion?){
        this.selectedConnexion = c
    }
    /** Methode pour recuperer la connexion selectionnée **/
    fun getselectedConnexion() : Connexion?{
        return this.selectedConnexion
    }
    /** Methode pour attribuer un nom au graphe **/
    fun setTitre(name:String){
        this.titre = name
    }

    /** Methode pour ajouter un noeud **/
    fun addNode(s: Node) {
        if (!(nodes.contains(s))) {
            nodes.add(s)
        }
    }

    fun getNode(x: Float, y: Float): Node? {
        var no: Node? = null
        for (n: Node in nodes) {
            if ((x >= n.getPosX() - 30F && x <= n.getPosX() + 30F) && (y >= n.getPosY() - 30F && y <= n.getPosY() + 30F)) {
                no = n
            }
        }
        return no
    }
    fun getConnexion(x: Float, y: Float): Connexion? {
        var co: Connexion? = null
        for (c:Connexion in connexions) {
           // Log.i("","x:$x |${c.getMiddle()[0]}||y:$y| ${c.getMiddle()[1]}")
               val f = c.getMiddle()
            if ((x>= c.getMiddle()[0]- 20F && x<= c.getMiddle()[0]+20F) && (y>= c.getMiddle()[1]- 20F && y<= c.getMiddle()[1]+20F) ) {
                co = c
            }
        }
        return co
    }
    fun getConnexion(c:Connexion): Connexion? {
        var con:Connexion?  = null
        for (co:Connexion in connexions) {
            if (co == c) {
                con=co
            }
        }
        return con
    }

    fun getNodeList(): ArrayList<Node> {
        return this.nodes
    }

    /**
     * Methode pour ajouter une connexion
     */
    fun addConnexion(c: Connexion) {
        if (!(connexions.contains(c))) {
            connexions.add(c)
        }
    }

    /**
     * Methode pour retourner les connexions du graphe
     */
    fun getConnexionList(): ArrayList<Connexion> {
        return this.connexions
    }

    /** methode pour renitialiser le graphe **/
    fun reset(){
        this.connexions.clear()
        this.nodes.clear()
        this.tmpConnexion = null
    }

    /** methode pour supprimer un noeud **/
    fun deleteNode(n:Node):Boolean{
        for (co:Connexion in connexions) {
            if (co.getEmitter() == n || co.getReceiver() == n) {
                this.connexions.remove(co)
            }
        }
        this.nodes.remove(n)
        return true
    }
    /** methode pour supprimer un noeud **/
    fun deleteConnexion(c:Connexion):Boolean{
        this.connexions.remove(c)
        return true
    }
}