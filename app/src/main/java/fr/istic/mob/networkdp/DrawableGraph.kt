package fr.istic.mob.networkdp

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph(private var ga: Graph) : Drawable() {
    private var c: Canvas = Canvas()
    private val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val rectPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pathtempPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        rectPaint.style = Paint.Style.FILL
        pathPaint.style = Paint.Style.STROKE
        pathtempPaint.style = Paint.Style.STROKE
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

    private fun drawConnexions(connexions: ArrayList<Connexion>) {
        var p: Path = Path()
        if (connexions.isNotEmpty()) {
            for (connexion in connexions) {
                pathPaint.strokeWidth = connexion.getEpaisseur()
                pathPaint.color = connexion.getcouleur()
                val pos: FloatArray = connexion.getMiddle()
                p.moveTo(connexion.getEmitter().getPosX(), connexion.getEmitter().getPosY())
                p.lineTo(connexion.getReceiver().getPosX(), connexion.getReceiver().getPosY())
                c.drawPath(p, pathPaint)
                c.drawText(connexion.getetiquette(), pos[0], pos[1], textPaint)
            }
        }
    }

    private fun drawTempConnexion(tempConnexion: Connexion?) {
        var p = Path()
        if (tempConnexion != null) {
            pathtempPaint.strokeWidth = tempConnexion.getEpaisseur()
            pathtempPaint.color = tempConnexion.getcouleur()
            p.moveTo(tempConnexion.getEmitter().getPosX(), tempConnexion.getEmitter().getPosY())
            p.lineTo(tempConnexion.getReceiver().getPosX(), tempConnexion.getReceiver().getPosY())
            c.drawPath(p, pathPaint)
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