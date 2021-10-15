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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.StringBuilder

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img =  findViewById<ImageView>(R.id.imageView)
       /* try {
            ga = gettofile()
            img.setImageDrawable(DrawableGraph(ga,null))
        }catch (e: IOException){
            ga = Graph()
            e.printStackTrace()
            Log.e("MyActivity", "erreur de lecture de old_data")
        }*/

     val old_data = this.getSharedPreferences("old_data", 0)
        if (old_data != null) {
            ga = Json.decodeFromString<Graph>("${old_data.getString("graph","").toString()}")
        }else{
            ga = Graph()
        }
        img.setImageDrawable(DrawableGraph(ga,null))

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
                var time:Long = 0
                /**
                 * implementation du listener
                 */
                img.setOnTouchListener {
                        _, event ->
                    //detect1.onTouchEvent(event)
                    when(event.action){
                        MotionEvent.ACTION_DOWN ->{
                           time = System.currentTimeMillis()
                        }
                        MotionEvent.ACTION_UP ->{
                            val time1 = System.currentTimeMillis()- time
                            if(time1>=1000){
                                Xp = event.x
                                Yp = event.y
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

                    }
                   true
                }


            }
            //Lorsqu'on clique sur Modifier objet
            R.id.reading_mode ->{
                item.setChecked(true)
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
                item.setChecked(true)
                this.title = "Graphs : Arc addition mode"
                mode = States.ADDING_CONNEXION
                var ndepart: Noeud? = null
                var ntp:Noeud? = null
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
                            img.setImageDrawable(DrawableGraph(ga, null))
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
                             ntp = Noeud(mx, my, "")
                            Log.i("","Down : ${ntp.toString()}")
                            var c: Connexion?
                            if (ndepart != null) {
                                c = Connexion(ndepart!!, ntp!!)
                                img.setImageDrawable(DrawableGraph(ga, c))
                            }
                        }

                    }
                    true
                    //detect2.onTouchEvent(event)
                }

            }
            //Lorsqu'on clique sur modifier connexion
            R.id.update ->{
                item.setChecked(true)
                this.title = "Graphs :Update Mode"
                mode = States.UPDATE_CONNEXION
            }
            //Lorsqu'on clique sur renitialiser le graphe
            R.id.reset ->{
                mode = States.RESET
                item.setChecked(true)
                this.title = "NetworkDP"
                ga = Graph()
                //item.itemId = R.id.reading_mode
                img.setImageDrawable(DrawableGraph(ga,null))
               // Toast.makeText(this,"Selectionner un mode dans le menu",500F)


            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Fonction pour ecrire le graphe dans un fichier old_data dans le cache
     */
    @Throws(IOException::class)
    fun saveintofile(){
        val outputStream: FileOutputStream = openFileOutput("old_data", MODE_PRIVATE)
        val json = Json.encodeToString(ga)
        outputStream.write(json.toByteArray())
        outputStream.close()
    }

    /**
     * Fonction pour recuperer le contenu de old_data
     */
    @Throws(IOException::class)
    fun gettofile():Graph{
        var value: String? = null
        val inputStream: FileInputStream = openFileInput("old_data")
        if(inputStream != null){
            val stringb = StringBuilder()
            var content: Int
            while (inputStream.read().also { content = it } != -1) {
                value = stringb.append(content.toChar()).toString()
            }
            val ga:Graph = Json.decodeFromString<Graph>("$value")
            return ga
        }else{
            return Graph()
        }



    }
    override fun onStop() {
        super.onStop()
       /* try {
            saveintofile()
        }catch (e: IOException){
            e.printStackTrace()
            Log.e("MyActivity", "erreur d'ecriture de old_data")
        }*/

        //creation et ecriture des données partagées
        val old_operation = this.getSharedPreferences("old_data", 0)
        val editor = old_operation.edit()
        val json = Json.encodeToString(ga)
        editor.putString("graph", json)
        editor.apply()
    }


}