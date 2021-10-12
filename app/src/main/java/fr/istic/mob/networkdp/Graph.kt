package fr.istic.mob.networkdp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Graph {
    private val noeuds: ArrayList<Noeud> = ArrayList<Noeud>()
    private val connexions: List<Connexion> = ArrayList<Connexion>()
    //val g: HashMap<Noeud, HashSet<Noeud>?> = HashMap()

    /**
     * Methode pour ajouter un noeud
     */
    fun addNoeud(s: Noeud) {
        if (!(noeuds.contains(s))) {
            noeuds.add(s)
        }
    }
    fun getNoeudList():ArrayList<Noeud>{
        return this.noeuds
    }
    /**
     * Methode pour ajouter une connexion
     */
    fun addConnexion(Source: Noeud, Destination: Noeud) {

    }


}