package fr.istic.mob.networkdp

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private  lateinit var  ga:Graph
    private var nom : String? = null
    private var  Xp : Float =0F
    private var Yp:Float = 0F
    private var mode:States = States.RESET
    private lateinit var detect1: GestureDetector
    private lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img =  findViewById<ImageView>(R.id.imageView)
        ga = Graph()
        /**
         * Création du detector pour le mode ajout de connexion
         */
        detect1 = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent?) {
                if (e != null) {
                    Xp=e.x
                    Yp=e.y
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
                    /**
                     * Création de la boite de dialogue et de ses actions
                     */
                    val alertDialog = AlertDialog.Builder(this)
                    val input = EditText(this)
                    //input.hint = "hint"
                    alertDialog.setTitle("title")
                    alertDialog.setMessage("Entrez le nom de l'objet")
                    alertDialog.setView(input)
                    alertDialog.setPositiveButton("Valider", DialogInterface.OnClickListener { dialog, which ->
                        /**
                         * methode du bouton Valider
                         */
                        val valsaisie = input.text.toString()
                        ga.addNoeud(Noeud(Xp,Yp,valsaisie))
                        img.setImageDrawable(DrawableGraph(ga))
                        // val d = DrawableGraph(ga)
                        // ga.refreshDrawableState()

                        // ga.invalidate()

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

                    //img.draw()
                    true
                }

            }
            //Lorsqu'on clique sur Modifier objet
            R.id.update_object ->{
                this.title = "Graphs : Sommet modification mode"
                mode = States.UPDATE_CONNEXION
            }
            //Lorsqu'on clique sur ajouter Connexion
            R.id.add_connect ->{
                this.title = "Graphs : Arc addition mode"
                mode = States.ADDING_CONNEXION
            }
            //Lorsqu'on clique sur modifier connexion
            R.id.update_connect ->{
                this.title = "Graphs : Arc modification mode"
                mode = States.UPDATE_CONNEXION
            }
            //Lorsqu'on clique sur renitialiser le graphe
            R.id.reset ->{
                mode = States.RESET

            }
            else->super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


}