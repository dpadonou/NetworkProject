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
    private  var planx:Float = 0F
    private  var plany:Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img =  findViewById<ImageView>(R.id.imageView)
        planx = img.height.toFloat()
        plany = img.width.toFloat()
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
            val m:States? = Json.decodeFromString<States>("${old_data.getString("mode","").toString()}")
           if(m != null){
                mode = m
                faire(mode)
                img.setImageDrawable(DrawableGraph(ga,null))
            }else{
                faire(mode)
                img.setImageDrawable(DrawableGraph(ga,null))
            }
            //img.setImageDrawable(DrawableGraph(ga,null))

        }else{
            ga = Graph()
            img.setImageDrawable(DrawableGraph(ga,null))
            //faire(mode)
        }


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
            mode = States.ADDING_NODE
            faire(mode)
            }
            //Lorsqu'on clique sur Modifier objet
            R.id.reading_mode ->{
                mode = States.READING_MODE
                faire(mode)
            }
            //Lorsqu'on clique sur ajouter Connexion
            R.id.add_connect ->{
                mode = States.ADDING_CONNEXION
                faire(mode)
            }
            //Lorsqu'on clique sur modifier connexion
            R.id.update ->{
                mode = States.UPDATE_MODE
                faire(mode)
            }
            //Lorsqu'on clique sur renitialiser le graphe
            R.id.reset ->{
                mode = States.RESET
                faire(mode)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /** Gerer les modes **/
    fun faire(m:States){
        when(m){
            States.ADDING_CONNEXION ->{
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name)).append(" - CONNEXION ADDING  Mode")
                this.title = sb.toString()
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
            States.ADDING_NODE ->{
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name)).append("- NODE ADDING Mode")
                this.title = sb.toString()
                this.title = sb.toString()
                this.title = sb.toString()
                var time:Long = 0
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
            States.UPDATE_MODE ->{
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name)).append(" - Update Mode")
                this.title = sb.toString()
                 //Rien pour le moment

            }
            States.READING_MODE ->{
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name)).append(" - READING MODE")
                this.title = sb.toString()
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
            States.RESET ->{
                this.title = resources.getString(R.string.app_name)
                ga = Graph()
                //item.itemId = R.id.reading_mode
                img.setImageDrawable(DrawableGraph(ga,null))
                // Toast.makeText(this,"Selectionner un mode dans le menu",500F)
            }
        }
    }

    /** Fonction pour ecrire le graphe dans un fichier old_data dans le cache **/
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
        editor.putString("mode", Json.encodeToString(mode))
        editor.apply()
    }


}