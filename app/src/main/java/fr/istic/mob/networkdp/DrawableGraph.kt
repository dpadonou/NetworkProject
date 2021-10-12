package fr.istic.mob.networkdp

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph:Drawable {
    private lateinit var ga:Graph

    constructor(ga: Graph) : super() {
        this.ga = ga
    }

    //  constructor() : super()

    fun setGraph(g:Graph){
        this.ga = g
    }

    override fun draw(p0: Canvas) {
        val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
        val rectp = Paint(Paint.LINEAR_TEXT_FLAG)
        rectp.style = Paint.Style.FILL
        rectp.color = Color.RED
        textPaint.color = Color.BLACK
        textPaint.textSize = 100F
        // canvas.drawText("hey", 288F, 350F, textPaint)
        for ((key, value) in ga.g) {
            p0.drawCircle(key.getPosx(), key.getPosy(), 40F, rectp)
            p0.drawText(key.getTitre(), key.getPosx(), key.getPosy(), textPaint)
        }
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