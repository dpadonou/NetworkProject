package fr.istic.mob.networkdp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Graph {
    private val noeuds: ArrayList<Noeud> = ArrayList<Noeud>()
    private val connexions: ArrayList<Connexion> = ArrayList<Connexion>()
    //val g: HashMap<Noeud, HashSet<Noeud>?> = HashMap()

    /**
     * Methode pour ajouter un noeud
     */
    fun addNoeud(s: Noeud) {
        if (!(noeuds.contains(s))) {
            noeuds.add(s)
        }
    }
    fun getNoeud(x:Float,y:Float): Noeud? {
        var no:Noeud? =null
        for(n:Noeud in noeuds){
           if((x>=n.getPosx()-30F && x<=n.getPosx()+30F) && (y>=n.getPosy()-30F && y<=n.getPosy()+30F)){
                no = n
            }
        }
        return no
    }
    fun getNoeudList():ArrayList<Noeud>{
        return this.noeuds
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
    fun getConnexionList():ArrayList<Connexion>{
        return this.connexions
    }




}