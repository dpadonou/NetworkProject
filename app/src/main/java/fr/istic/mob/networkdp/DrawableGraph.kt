package fr.istic.mob.networkdp

import android.graphics.*
import android.graphics.drawable.Drawable

class DrawableGraph:Drawable {
    private lateinit var ga:Graph
    private var  c:Canvas = Canvas()
    private  var tempConnexion:Connexion? = null
    val textPaint = Paint(Paint.LINEAR_TEXT_FLAG)
    val rectp = Paint(Paint.LINEAR_TEXT_FLAG)
    val pathp = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    val path:Path = Path()


    constructor(ga: Graph,c:Connexion?) : super() {
        rectp.style = Paint.Style.FILL
        rectp.color = Color.RED
        pathp.color = Color.YELLOW
        pathp.strokeWidth = 20F
        textPaint.color = Color.BLACK
        textPaint.textSize = 40F
        this.ga = ga
        this.tempConnexion = c
    }

    //  constructor() : super()

    fun setGraph(g:Graph){
        this.ga = g
    }
    fun settempConnexion(t:Connexion){
        this.tempConnexion = t
    }
    fun gettempConnexion(): Connexion? {
        return this.tempConnexion
    }
    fun drawNoeuds(n:ArrayList<Noeud>){
        if(n.isNotEmpty()){
            for (i in n){
                c.drawCircle(i.getPosx(), i.getPosy(), 30F, rectp)
                c.drawText(i.getTitre(), i.getPosx(), i.getPosy(), textPaint)
            }
        }

    }
    fun drawConnexions(n:ArrayList<Connexion>){
        if(n.isNotEmpty()){
            for (i in n){
                c.drawLine(i.getEmetteur().getPosx(),i.getEmetteur().getPosy(),i.getReceiver().getPosx(),i.getReceiver().getPosy(),pathp)
            }
        }
    }
    fun drawtempConnexion(){
        //path.moveTo(0F,0F)
        //path.lineTo(0F,0F)
        if(tempConnexion!=null){
            //path.moveTo(tempConnexion!!.getEmetteur().getPosx(),tempConnexion!!.getEmetteur().getPosy())
            //path.lineTo(tempConnexion!!.getReceiver().getPosx(),tempConnexion!!.getReceiver().getPosy())
            //c.drawPath(path,pathp)
           c.drawLine(tempConnexion!!.getEmetteur().getPosx(),tempConnexion!!.getEmetteur().getPosy(),tempConnexion!!.getReceiver().getPosx(),tempConnexion!!.getReceiver().getPosy(),pathp)
        }

    }

    override fun draw(p0: Canvas) {
         this.c=p0
        drawNoeuds(ga.getNoeudList())
        drawtempConnexion()
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