package fr.istic.mob.networkdp

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph(private var ga: Graph) : Drawable() {
    private var c: Canvas = Canvas()
    private val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val rectPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pathTempPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        rectPaint.style = Paint.Style.FILL
        pathPaint.style = Paint.Style.STROKE
        pathTempPaint.style = Paint.Style.STROKE
        textPaint.color = Color.BLACK
        textPaint.textSize = 40F
    }

    private fun drawNodes(n: ArrayList<Node>) {
        if (n.isNotEmpty()) {
            for (i in n) {
                rectPaint.color = i.getcouleur()
                c.drawCircle(i.getPosX(), i.getPosY(), 30F, rectPaint)
                c.drawText(i.getTitre(), i.getPosX(), i.getPosY(), textPaint)
            }
        }
    }

    private fun ArrayList<Connexion>.drawConnexions() {
        val p = Path()
        if (isNotEmpty()) {
            for (connexion in this) {
                pathPaint.strokeWidth = connexion.getEpaisseur()
                pathPaint.color = connexion.getColor()
                val pos: FloatArray = connexion.getMiddle()
                p.moveTo(connexion.getEmitter().getPosX(), connexion.getEmitter().getPosY())
                p.lineTo(connexion.getReceiver().getPosX(), connexion.getReceiver().getPosY())
                c.drawPath(p, pathPaint)
                c.drawText(connexion.getEtiquette(), pos[0], pos[1], textPaint)
            }
        }
    }

    private fun drawTempConnexion(tempConnexion: Connexion?) {
        val p = Path()
        if (tempConnexion != null) {
            pathTempPaint.strokeWidth = tempConnexion.getEpaisseur()
            pathTempPaint.color = tempConnexion.getColor()
            p.moveTo(tempConnexion.getEmitter().getPosX(), tempConnexion.getEmitter().getPosY())
            p.lineTo(tempConnexion.getReceiver().getPosX(), tempConnexion.getReceiver().getPosY())
            c.drawPath(p, pathPaint)
        }
    }

    override fun draw(p0: Canvas) {
        this.c = p0
        drawNodes(ga.getNodeList())
        drawTempConnexion(ga.tmpConnexion)
        ga.getConnexionList().drawConnexions()
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