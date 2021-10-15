package fr.istic.mob.networkdp

class Connexion {
    private lateinit var debut:Noeud
    private lateinit var  fin:Noeud
    constructor()

    constructor(debut: Noeud, fin: Noeud) {
        this.debut = debut
        this.fin = fin
    }

    /**
     * getters and setters for debut
     */
    fun getEmetteur():Noeud{
        return this.debut
    }
    private fun setEmetteur(n:Noeud){
        this.debut= n
    }

    /**
     * getters and setters for fin
     */
    fun getReceiver():Noeud{
        return this.fin
    }
   fun setReceiver(n:Noeud){
        this.debut= n
    }

}