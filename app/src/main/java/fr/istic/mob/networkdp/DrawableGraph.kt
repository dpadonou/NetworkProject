package fr.istic.mob.networkdp

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph(private var ga: Graph) : Drawable() {
    private var c: Canvas = Canvas()
    private val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val rectPaint = Paint(Paint.LINEAR_TEXT_FLAG)

    init {
        rectPaint.style = Paint.Style.FILL
        textPaint.color = Color.BLACK
        textPaint.textSize = 40F
    }

    /** Dessine les noeuds du graphe **/
    private fun drawNodes(n: ArrayList<Node>) {
        if (n.isNotEmpty()) {
            for (i in n) {
                rectPaint.color = i.getcouleur()
                val rect = RectF(i.getPosX()-50, i.getPosY()+30, i.getPosX()+50, i.getPosY()-30)
                c.drawRoundRect(rect, 20F, 20F, rectPaint)
                textPaint.textAlign = Paint.Align.CENTER
                c.drawText(i.getTitre(), rect.centerX(), rect.centerY(), textPaint)
            }
        }
    }

    /** Dessine les connexions du graphe **/
    private fun drawConnexions(n: ArrayList<Connexion>) {
        val p = Path()
        if (n.isNotEmpty()) {
            for (i in n) {
                val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                pathPaint.style = Paint.Style.STROKE
                pathPaint.strokeWidth = i.getEpaisseur()
                pathPaint.color = i.getcouleur()
                val pos: FloatArray = i.getMiddle()
                p.moveTo(i.getEmitter().getPosX(), i.getEmitter().getPosY())
                if (i.isCurved) {
                    p.quadTo(
                        (pos[0] + i.mx) / 2,
                        (pos[1] + i.my) / 2,
                        i.getReceiver().getPosX(),
                        i.getReceiver().getPosY()
                    )
                    c.drawText(
                        i.getetiquette(),
                        (pos[0] + i.mx) / 2,
                        (pos[1] + i.my) / 2,
                        textPaint
                    )
                } else {
                    p.lineTo(i.getReceiver().getPosX(), i.getReceiver().getPosY())
                    textPaint.textAlign = Paint.Align.CENTER
                    c.drawText(i.getetiquette(), pos[0], pos[1], textPaint)
                }
                c.drawPath(p, pathPaint)
            }
        }
    }

    /** dessine la connexion temporaire **/
    private fun drawTempConnexion(tempConnexion: Connexion?) {
        val p = Path()
        val pathtempPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pathtempPaint.style = Paint.Style.STROKE
        if (tempConnexion != null) {
            pathtempPaint.strokeWidth = tempConnexion.getEpaisseur()
            pathtempPaint.color = tempConnexion.getcouleur()
            p.moveTo(tempConnexion.getEmitter().getPosX(), tempConnexion.getEmitter().getPosY())
            //p.quadTo(tempConnexion.getEmitter().getPosX(),tempConnexion.getEmitter().getPosY(),tempConnexion.getReceiver().getPosX(),tempConnexion.getReceiver().getPosY())
            p.lineTo(tempConnexion.getReceiver().getPosX(), tempConnexion.getReceiver().getPosY())
            c.drawPath(p, pathtempPaint)
        }
    }

    override fun draw(p0: Canvas) {
        this.c = p0
        drawNodes(ga.getNodeList())
        drawConnexions(ga.getConnexionList())
        drawTempConnexion(ga.gettmpConnexion())
    }

    override fun setAlpha(p0: Int) {
    }

    override fun setColorFilter(p0: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}