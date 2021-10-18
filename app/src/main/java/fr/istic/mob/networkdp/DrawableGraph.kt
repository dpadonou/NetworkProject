package fr.istic.mob.networkdp

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph(private var ga: Graph) : Drawable() {
    private var c: Canvas = Canvas()
    private val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val rectPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val pathPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    init {
        rectPaint.style = Paint.Style.FILL
        rectPaint.color = Color.RED
        pathPaint.color = Color.YELLOW
        pathPaint.strokeWidth = 20F
        textPaint.color = Color.BLACK
        textPaint.textSize = 40F
    }

    private fun drawNodes(n: ArrayList<Node>) {
        if (n.isNotEmpty()) {
            for (i in n) {
                c.drawCircle(i.getPosX(), i.getPosY(), 30F, rectPaint)
                c.drawText(i.getTitre(), i.getPosX(), i.getPosY(), textPaint)
            }
        }
    }

    private fun drawConnexions(n: ArrayList<Connexion>) {
        if (n.isNotEmpty()) {
            for (i in n) {
                c.drawLine(
                    i.getEmitter().getPosX(),
                    i.getEmitter().getPosY(),

                    i.getReceiver().getPosX(),
                    i.getReceiver().getPosY(),
                    pathPaint
                )
            }
        }
    }

    private fun drawTempConnexion(tempConnexion:Connexion?) {
        if (tempConnexion != null) {
            c.drawLine(
                tempConnexion!!.getEmitter().getPosX(),
                tempConnexion!!.getEmitter().getPosY(),
                tempConnexion!!.getReceiver().getPosX(),
                tempConnexion!!.getReceiver().getPosY(),
                pathPaint
            )
        }
    }

    override fun draw(p0: Canvas) {
        this.c = p0
        drawNodes(ga.getNodeList())
        drawTempConnexion(ga.tmpConnexion)
        drawConnexions(ga.getConnexionList())
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