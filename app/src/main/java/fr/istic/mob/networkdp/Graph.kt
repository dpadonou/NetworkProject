package fr.istic.mob.networkdp

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
            if ((x >= n.getPosX() - 50F && x <= n.getPosX() + 50F) && (y >= n.getPosY() - 30F && y <= n.getPosY() + 30F)) {
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
        for (co: Connexion in connexions) {
            if (co.getEmitter() == n || co.getReceiver() == n) {
                this.connexions.remove(co)
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