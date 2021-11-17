package fr.istic.mob.networkDP

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
        if (n.isNotEmpty()) {
            for (i in n) {
                pathPaint.strokeWidth = i.getThickness()
                pathPaint.color = i.getColor()
                c.drawPath(i.getPath(), pathPaint)
                c.drawText(i.getTag(), i.getMiddle()[0], i.getMiddle()[1], textPaint)
                /*pathPaint.strokeWidth = i.getEpaisseur()
                pathPaint.color = i.getcouleur()
                val pos: FloatArray = i.getMiddle()
                p.moveTo(i.getEmitter().getPosX(), i.getEmitter().getPosY())
                if (i.isCurved) {
                    p.quadTo(
                        (pos[0] + i.mX) / 2,
                        (pos[1] + i.mY) / 2,
                        i.getReceiver().getPosX(),
                        i.getReceiver().getPosY()
                    )
                    c.drawText(
                        i.getetiquette(),
                        (pos[0] + i.mX) / 2,
                        (pos[1] + i.mY) / 2,
                        textPaint
                    )
                    i.setMiddle((pos[0] + i.mX) / 2,(pos[1] + i.mY) / 2)
                } else {
                    p.lineTo(i.getReceiver().getPosX(), i.getReceiver().getPosY())
                    //textPaint.textAlign = Paint.Align.CENTER
                    c.drawText(i.getetiquette(), pos[0], pos[1], textPaint)
                }
                c.drawPath(p, pathPaint)*/
            }
        }
    }

    /** dessine la connexion temporaire **/
    private fun drawTempConnexion(tempConnexion: Connexion?) {
        val p = Path()
        if (tempConnexion != null) {
            pathtempPaint.strokeWidth = tempConnexion.getThickness()
            pathtempPaint.color = tempConnexion.getColor()
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