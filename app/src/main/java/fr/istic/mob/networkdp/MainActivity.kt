package fr.istic.mob.networkdp

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private  lateinit var  ga:Graph
    private var nom : String? = null
    private var  Xp : Float =0F
    private var Yp:Float = 0F
    private var mode:States = States.RESET
    private lateinit var detect1: GestureDetector
    private lateinit var detect2: GestureDetector
    private lateinit var img: ImageView
    private var downx:Float = 0F
    private var downy:Float = 0F
    private var upx:Float = 0F
    private var upy:Float = 0F
    private var mx:Float = 0F
    private var my:Float = 0F
   // private lateinit var d:DrawableGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img =  findViewById<ImageView>(R.id.imageView)
        ga = Graph()
       // d = DrawableGraph(ga)
        /**
         * Création du detector pour le mode ajout de noeud
         */
        detect1 = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent?) {
                if (e != null) {
                    Xp = e.x
                    Yp = e.y
                    /**
                     * Création de la boite de dialogue et de ses actions
                     */
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    val input = EditText(this@MainActivity)
                    //input.hint = "hint"
                    alertDialog.setTitle("title")
                    alertDialog.setMessage("Entrez le nom de l'objet")
                    alertDialog.setView(input)
                    val positiveButton = alertDialog.setPositiveButton(
                        "Valider",
                        DialogInterface.OnClickListener { dialog, which ->
                            /**
                             * methode du bouton Valider
                             */
                            val valsaisie = input.text.toString()
                            ga.addNoeud(Noeud(Xp, Yp, valsaisie))
                            img.setImageDrawable(DrawableGraph(ga,null))
                            dialog.dismiss()

                        })
                    alertDialog.setNegativeButton("Fermer", DialogInterface.OnClickListener { dialog, which ->
                        /**
                         * methode du bouton fermer de la boite de dialogue
                         */
                        dialog.dismiss()
                    })
                    /**
                     * Affichage de la boite de dialogue
                     */
                    alertDialog.show()
                }
            }
        })
        /**
         * Création du detector pour le mode ajout de connexion
         */
        detect2 = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent?) {
                when(e?.action){
                    MotionEvent.ACTION_DOWN ->{
                        downx = e?.x
                        downy = e?.y
                    }
                    MotionEvent.ACTION_HOVER_MOVE ->{
                        mx = e?.x
                        my = e?.y
                        val ndepart: Noeud? = ga.getNoeud(downx, downy)
                        val nfin: Noeud = Noeud(mx, my, "")
                        var c: Connexion?
                        if (ndepart != null) {
                            c = Connexion(ndepart, nfin)
                            img.setImageDrawable(DrawableGraph(ga, c))
                        }
                    }
                    MotionEvent.ACTION_UP ->{
                        upx = e?.x
                        upy = e?.y
                    }

                }
            }

        })
    }

    /**
     * Creation du menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }
    /**
     * Implementation des options du menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //Lorsqu'on clique sur ajouter objet
            R.id.add_object -> {
                this.title = "Graphs : Sommet addition mode"
                mode = States.ADDING_NODE
                //var valsaisie : String?
                // val mydetect :GestureDetector = GestureDetector(this,GestureDetector())
                /**
                 * implementation du listener
                 */
                img.setOnTouchListener {
                        _, event -> detect1.onTouchEvent(event)

                }


            }
            //Lorsqu'on clique sur Modifier objet
            R.id.reading_mode ->{
                this.title = "Graphs : Mode lecture"
                mode = States.READING_MODE
                var selectNoeud:Noeud? = null
                img.setOnTouchListener { _, event ->

                    when(event.action){
                        MotionEvent.ACTION_DOWN ->{
                            downx = event.x
                            downy = event.y
                            selectNoeud = ga.getNoeud(downx,downy)
                        }
                        MotionEvent.ACTION_MOVE ->{
                            mx = event.x
                            my = event.y
                            if(selectNoeud !=null){
                                selectNoeud!!.setPosy(my)
                                selectNoeud!!.setPosx(mx)
                                img.invalidate()
                                img.setImageDrawable(DrawableGraph(ga,null))
                            }
                        }

                    }
                    true
                    //detect2.onTouchEvent(event)
                }
            }
            //Lorsqu'on clique sur ajouter Connexion
            R.id.add_connect ->{
                this.title = "Graphs : Arc addition mode"
                mode = States.ADDING_CONNEXION
                var ndepart: Noeud? = null
                lateinit var nfin: Noeud
                img.setOnTouchListener { _, event ->
                    /*for (n:Noeud in ga.getNoeudList()){
                        Log.i("","Noeud : ${n.getPosx()} - ${n.getPosy()}")
                    }*/
                    when(event.action){
                        MotionEvent.ACTION_DOWN ->{
                            downx = event.x
                            downy = event.y
                            ndepart= ga.getNoeud(downx, downy)
                            Log.i("","Down : ${ndepart.toString()}")
                        }
                        MotionEvent.ACTION_UP ->{
                            upx = event.x
                            upy = event.y
                            val nfintp: Noeud? = ga.getNoeud(upx, upy)
                            Log.i("","Up : ${nfintp.toString()}")
                            if (nfintp != null){
                                nfin = nfintp
                                img.invalidate()
                                ga.addConnexion(Connexion(ndepart!!,nfin))
                                img.setImageDrawable(DrawableGraph(ga,null))
                            }
                            Log.i("","Up: $upx - $upy")
                        }
                        MotionEvent.ACTION_MOVE ->{
                            mx = event.x
                            my = event.y
                            //Log.i("","Move : $mx - $my")
                            //Log.i("","ndepart : ${ndepart.toString()}")
                            val ntp: Noeud = Noeud(mx, my, "")
                            Log.i("","Down : ${ntp.toString()}")
                            var c: Connexion?
                            if (ndepart != null) {
                                c = Connexion(ndepart!!, ntp)
                                img.setImageDrawable(DrawableGraph(ga, c))
                            }
                        }

                    }
                    true
                    //detect2.onTouchEvent(event)
                }

            }
            //Lorsqu'on clique sur modifier connexion
            R.id.update_connect ->{
                this.title = "Graphs :mode modification"
                mode = States.UPDATE_CONNEXION
            }
            //Lorsqu'on clique sur renitialiser le graphe
            R.id.reset ->{
                mode = States.RESET
                ga = Graph()
                img.setImageDrawable(DrawableGraph(ga,null))

            }
            else->super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }



}