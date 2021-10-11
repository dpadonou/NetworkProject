package fr.istic.mob.networkdp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Graph: View {
    val g: HashMap<Noeud, HashSet<Noeud>?> = HashMap()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    /**
     * Methode pour ajouter un noeud
     */
    fun addNoeud(s: Noeud) {
        if (!(g.containsKey(s))) {
            g[s] = null
        }
    }
    /**
     * Methode pour ajouter une connexion
     */
    fun addConnexion(Source: Noeud, Destination: Noeud) {

    }
    //methode pour faire les dessins
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * Création des Paint peintures
         */
        val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
        val rectp = Paint(Paint.LINEAR_TEXT_FLAG)
        rectp.style = Paint.Style.FILL
        rectp.color = Color.RED
        textPaint.color = Color.BLACK
        textPaint.textSize = 100F
        /**
         * Création des dessins à proprement parlé
         */
        canvas.drawCircle(288F, 350F, 30F, rectp)
        // canvas.drawText("hey", 288F, 350F, textPaint)
        for ((key, value) in g) {
            canvas.drawCircle(key.getPosx(), key.getPosy(), 40F, rectp)
            canvas.drawText(key.getTitre(), key.getPosx(), key.getPosy(), textPaint)
        }
        //g.forEach { (key, value) -> Log.i("","$key = $value") }

    }


}