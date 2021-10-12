package fr.istic.mob.networkdp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Graph {
    val g: HashMap<Noeud, HashSet<Noeud>?> = HashMap()

    /**
     * Methode pour ajouter un noeud
     */
    fun addNoeud(s: Noeud) {
        if (!(g.containsKey(s))) {
            g[s] = null
        }
    }
    /**
     * Methode pour ajouter une connexion
     */
    fun addConnexion(Source: Noeud, Destination: Noeud) {

    }


}