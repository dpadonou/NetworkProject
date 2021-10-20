package fr.istic.mob.networkdp

import kotlinx.serialization.Serializable

@Serializable
class Graph {
    private var titre:String = ""
    private val nodes: ArrayList<Node> = ArrayList()
    private val connexions: ArrayList<Connexion> = ArrayList()
    var tmpConnexion:Connexion? = null

    constructor(titre: String) {
        this.titre = titre
    }

    /**
     * Methode pour ajouter un noeud
     */
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
}