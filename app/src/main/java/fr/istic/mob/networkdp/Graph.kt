package fr.istic.mob.networkDP

import android.graphics.RectF
import kotlinx.serialization.Serializable

@Serializable
class Graph {
    private var titre: String = ""
    private val nodes: ArrayList<Node> = ArrayList()
    private val connexions: ArrayList<Connexion> = ArrayList()
    private var tmpConnexion: Connexion? = null

    /** Methode pour recuperer le titre **/
    fun getTitre(): String {
        return this.titre
    }

    /** Methode pour attribuer une connexion temporaire **/
    fun settmpConnexion(c: Connexion?) {
        this.tmpConnexion = c
    }

    /** Methode pour recuperer la connexion temporaire **/
    fun gettmpConnexion(): Connexion? {
        return this.tmpConnexion
    }

    /** Methode pour attribuer un nom au graphe **/
    fun setTitre(name: String) {
        this.titre = name
    }

    /** Methode pour ajouter un noeud **/
    fun addNode(s: Node) {
        if (!(nodes.contains(s))) {
            nodes.add(s)
        }
    }

    /** recupérer un noeud du graphe par la position du centre **/
    fun getNode(x: Float, y: Float): Node? {
        var node: Node? = null
        for (n: Node in nodes) {
            val rect = RectF(n.getPosX()-50, n.getPosY()+30, n.getPosX()+50, n.getPosY()-30)
            if(x >= rect.left && x < rect.right && y <= rect.top && y > rect.bottom) {
                node = n
            }
        }
        return node
    }

    /** recuprérer une connexion du graphe par la position du milieu **/
    fun getGraphConnexionByMiddlePosition(x: Float, y: Float): Connexion? {
        var connexion: Connexion? = null
        for (c in connexions) {
            if ((x >= c.getMiddle()[0] - 20F && x <= c.getMiddle()[0] + 20F)
                && (y >= c.getMiddle()[1] - 20F && y <= c.getMiddle()[1] + 20F)
            ) {
                connexion = c
            }
        }
        return connexion
    }

    /** Verifier si une connexion appartient au graphe **/
    fun getGraphConnexion(c: Connexion): Connexion? {
        var connexion: Connexion? = null
        for (co: Connexion in connexions) {
            if (co == c) {
                connexion = co
            }
        }
        return connexion
    }

    /** Retourne la liste des noeuds du graphe **/
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
    fun reset() {
        this.connexions.clear()
        this.nodes.clear()
        this.tmpConnexion = null
    }

    /** methode pour supprimer un noeud **/
    fun deleteNode(n: Node): Boolean {
        val iterator = connexions.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            if (item.getEmitter() == n || item.getReceiver() == n) {
                iterator.remove()
            }
        }

        this.nodes.remove(n)
        return true
    }

    /** methode pour supprimer un noeud **/
    fun deleteConnexion(c: Connexion): Boolean {
        this.connexions.remove(c)
        return true
    }
}