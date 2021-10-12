package fr.istic.mob.networkdp

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph:Drawable {
    private lateinit var ga:Graph
    private var  c:Canvas = Canvas()
    val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    val rectp = Paint(Paint.LINEAR_TEXT_FLAG)


    constructor(ga: Graph) : super() {
        rectp.style = Paint.Style.FILL
        rectp.color = Color.RED
        textPaint.color = Color.BLACK
        textPaint.textSize = 100F
        this.ga = ga
    }

    //  constructor() : super()

    fun setGraph(g:Graph){
        this.ga = g
    }
    fun drawNoeuds(n:ArrayList<Noeud>){
        if(n.isNotEmpty()){
            for (i in n){
                c.drawCircle(i.getPosx(), i.getPosy(), 40F, rectp)
                c.drawText(i.getTitre(), i.getPosx(), i.getPosy(), textPaint)
            }
        }

    }

    override fun draw(p0: Canvas) {
         this.c=p0
        drawNoeuds(ga.getNoeudList())
    }

    override fun setAlpha(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(p0: ColorFilter?) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}