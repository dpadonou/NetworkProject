package fr.istic.mob.networkdp

class Noeud {
    private lateinit var titre : String
    private  var posx : Float = 0F
    private  var posy : Float = 0F

    constructor(posx:Float,posy:Float, titre: String)  {
        this.titre = titre
        this.posx=posx
        this.posy=posy
    }


    fun getTitre(): String
    {
        return this.titre
    }
    fun getPosx(): Float
    {
        return this.posx
    }
    fun getPosy(): Float
    {
        return this.posy
    }
}