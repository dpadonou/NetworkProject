package fr.istic.mob.networkdp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private lateinit var ga: Graph
    private var xP: Float = 0F
    private var yP: Float = 0F
    private var mode: States = States.RESET
    private lateinit var img: ImageView
    private var downX: Float = 0F
    private var downY: Float = 0F
    private var upx: Float = 0F
    private var upy: Float = 0F
    private var mx: Float = 0F
    private var my: Float = 0F
    private var imgHeight: Float = 0F
    private var imgWidth: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img = findViewById(R.id.imageView)

        //imgWidth = img.measuredWidth.toFloat()
        ga = Graph()


        img.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
            imgWidth = img.measuredWidth.toFloat()
            imgHeight = img.measuredHeight.toFloat()
        })
        if(savedInstanceState!=null){
            val json = savedInstanceState.getSerializable("graph")
            val json2 = savedInstanceState.getSerializable("mode")
            ga = Json.decodeFromString("$json")
            mode = Json.decodeFromString("$json2")
            img.invalidate()
            val d = DrawableGraph(ga)
            img.setImageDrawable(d)
            faire(mode)
        }else{
            val d = DrawableGraph(ga)
            img.setImageDrawable(d)
        }


           /* try {
                ga = gettofile()
                img.setImageDrawable(DrawableGraph(ga))
            }catch (e: IOException){
                ga = Graph()
                e.printStackTrace()
                Log.e("MyActivity", "erreur de lecture de old_data")
            }*/
       ///////////////////

       /* val oldData = this.getSharedPreferences("old", 0)
        if (oldData != null) {
            ga = Json.decodeFromString(oldData.getString("graph", "").toString())
            val m: States? = Json.decodeFromString<States>(oldData.getString("mode", "").toString())
            if (m != null) {
                mode = m
                faire(mode)
                img.setImageDrawable(DrawableGraph(ga))
            } else {
                faire(mode)
                img.setImageDrawable(DrawableGraph(ga))
            }
        } else {
            ga = Graph()
            img.setImageDrawable(DrawableGraph(ga))
        }*/
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
            R.id.reading_mode -> {
                mode = States.READING_MODE
                faire(mode)
            }
            //Lorsqu'on clique sur ajouter Connexion
            R.id.add_connect -> {
                mode = States.ADDING_CONNEXION
                faire(mode)
            }
            //Lorsqu'on clique sur modifier connexion
            R.id.update -> {
                mode = States.UPDATE_MODE
                faire(mode)
            }
            //Lorsqu'on clique sur renitialiser le graphe
            R.id.reset -> {
                mode = States.RESET
                faire(mode)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Gerer les modes **/
    @SuppressLint("ClickableViewAccessibility")
    fun faire(m: States) {
        when (m) {
            States.ADDING_CONNEXION -> {
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name))
                    .append(" - "+ resources.getString(R.string.add_connect_text))
                this.title = sb.toString()
                var ndepart: Node? = null
                var ntp: Node?
                var tmpconnexion: Connexion?
                lateinit var nfin: Node
                img.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            downX = event.x
                            downY = event.y
                            ndepart = ga.getNode(downX, downY)
                            Log.i("", "Down : ${ndepart.toString()}")
                        }
                        MotionEvent.ACTION_UP -> {
                            upx = event.x
                            upy = event.y
                            ga.tmpConnexion = null
                           // img.setImageDrawable(DrawableGraph(ga))
                            img.invalidate()
                            val nfintp: Node? = ga.getNode(upx, upy)
                            Log.i("", "Up : ${nfintp.toString()}")
                            if (nfintp != null) {
                                nfin = nfintp
                                ga.addConnexion(Connexion(ndepart!!, nfin))
                                img.invalidate()
                               // img.setImageDrawable(DrawableGraph(ga))
                            }
                            Log.i("", "Up: $upx - $upy")
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mx = event.x
                            my = event.y
                            ntp = Node(mx, my, "")
//                            Log.i("", "Down : ${ntp.toString()}")
                            val c: Connexion?
                            if (ndepart != null) {
                                ga.tmpConnexion = Connexion(ndepart!!, ntp!!)
                                img.invalidate()
                                //img.setImageDrawable(DrawableGraph(ga))
                            }
                        }
                    }
                    true
                }
            }
            States.ADDING_NODE -> {
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name)).append(" - "+ resources.getString(R.string.add_object_text))
                this.title = sb.toString()
                var time: Long = 0
                img.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            time = System.currentTimeMillis()
                        }
                        MotionEvent.ACTION_UP -> {
                            val time1 = System.currentTimeMillis() - time
                            if (time1 >= 600) {
                                xP = event.x
                                yP = event.y
                                if((xP>=30F && xP<=imgWidth-30F) && (yP>=30F && yP<=imgHeight-30F) ){
                                //Création de la boite de dialogue et de ses actions
                              //  if((xP>=30F && xP<=imgWidth-30F) && (yP>=30F && yP<=imgHeight-30F) ){
                                val alertDialog = AlertDialog.Builder(this@MainActivity)
                                val input = EditText(this@MainActivity)
                                alertDialog.setTitle(resources.getString(R.string.noeud_etiquette))
                                alertDialog.setMessage(resources.getString(R.string.dialog_text))
                                alertDialog.setView(input)
                                alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                                    //methode du bouton Valider
                                    val valsaisie = input.text.toString()
                                    if(ga.getNode(xP,yP) == null){
                                        ga.addNode(Node(xP, yP, valsaisie))
                                        //img.invalidate()
                                        img.setImageDrawable(DrawableGraph(ga))

                                    }else{
                                        Toast.makeText(this,"Vous ne pouvez dessiner au meme emplacement",Toast.LENGTH_LONG).show()
                                    }
                                    dialog.dismiss()
                                }
                                alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                               // }
                            }else{
                                    Toast.makeText(this,"restez dans le cadre",Toast.LENGTH_LONG).show()

                                }
                            }
                        }
                    }
                    true
                }
            }
            States.UPDATE_MODE -> {
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name)).append(" - "+ resources.getString(R.string.update))
                this.title = sb.toString()
            }
            States.READING_MODE -> {
                val sb = StringBuilder()
                sb.append(resources.getString(R.string.app_name)).append(" - "+ resources.getString(R.string.reading_text))
                this.title = sb.toString()
                var selectNode: Node? = null
                img.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            downX = event.x
                            downY = event.y
                            selectNode = ga.getNode(downX, downY)
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mx = event.x
                            my = event.y
                            if((mx>=30F && mx<=imgWidth-30F) && (my>=30F && my<=imgHeight-30F) ){
                                if (selectNode != null) {
                                    selectNode!!.setPosY(my)
                                    selectNode!!.setPosX(mx)
                                    img.invalidate()
                                    // img.setImageDrawable(DrawableGraph(ga))
                                }
                            }

                        }
                    }
                    true
                }
            }
            States.RESET -> {
                this.title = resources.getString(R.string.app_name)
                ga = Graph()
                img.setImageDrawable(DrawableGraph(ga))
            }
        }
    }

    /** Fonction pour ecrire le graphe dans un fichier old_data dans le cache **/
    @Throws(IOException::class)
    fun saveintofile() {
        val outputStream: FileOutputStream = openFileOutput("old_data", MODE_PRIVATE)
        val json = Json.encodeToString(ga)
        outputStream.write(json.toByteArray())
        outputStream.close()
    }

    /**
     * Fonction pour recuperer le contenu de old_data
     */
    @Throws(IOException::class)
    fun gettofile(): Graph {
        var value: String? = null
        val inputStream: FileInputStream = openFileInput("old_data")
        return run {
            val stringb = StringBuilder()
            var content: Int
            while (inputStream.read().also { content = it } != -1) {
                value = stringb.append(content.toChar()).toString()
            }
            Json.decodeFromString("$value")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        val json = Json.encodeToString(ga)
        val json2 = Json.encodeToString(mode)
        outState.putSerializable("graph",json)
        outState.putSerializable("mode",json2)
        super.onSaveInstanceState(outState)
    }
    override fun onStop() {
        super.onStop()
      /*  try {
           saveintofile()
       }catch (e: IOException){
           e.printStackTrace()
           Log.e("MyActivity", "erreur d'ecriture de old_data")
       }*/


        //creation et ecriture des données partagées
      /* val oldOperation = this.getSharedPreferences("old", 0)
        val editor = oldOperation.edit()
        val json = Json.encodeToString(ga)
        editor.putString("graph", json)
        editor.putString("mode", Json.encodeToString(mode))
        editor.apply()*/
    }
}
