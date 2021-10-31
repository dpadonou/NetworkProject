package fr.istic.mob.networkdp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import java.io.*

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build

import android.os.Environment
import android.view.*
import android.widget.*


class MainActivity : AppCompatActivity() {
    private lateinit var ga: Graph
    private var xP: Float = 0F
    private var yP: Float = 0F
    private var mode: States = States.RESET
    private lateinit var img: ImageView
    private lateinit var graphTitre: TextView
    private var downX: Float = 0F
    private var downY: Float = 0F
    private var upx: Float = 0F
    private var upy: Float = 0F
    private var mx: Float = 0F
    private var my: Float = 0F
    private var imgHeight: Float = 0F
    private var imgWidth: Float = 0F
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var name:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        graphTitre = findViewById(R.id.graphTitre)
        img = findViewById(R.id.imageView)
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
            GraphTitreDialog()
            Toast.makeText(this,resources.getString(R.string.dialoggraph_msg),Toast.LENGTH_LONG).show()
            val d = DrawableGraph(ga)
            img.setImageDrawable(d)
        }


    }
    fun getimg():ImageView{
        return this.img
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
            //Lorsqu'on clique sur Sauvegarder le graphe
            R.id.save -> {
                mode = States.SAVE
                faire(mode)
            }
            //Lorsqu'on clique sur importer un graphe
            R.id.import_graph -> {
                mode = States.IMPORT_NETWORK
                faire(mode)
            }
            //Lorsqu'on clique sur importer de nouveau plan
            R.id.import_plan -> {
                mode = States.IMPORT_PLAN
                faire(mode)
            }
            //Lorsqu'on clique sur envoyer la capture du graphe
            R.id.send_network -> {
                mode = States.SEND_NETWORK
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
                            //img.invalidate()
                            img.setImageDrawable(DrawableGraph(ga))
                            val nfintp: Node? = ga.getNode(upx, upy)
                            if (nfintp != null) {
                                nfin = nfintp
                                val c = Connexion(ndepart!!, nfin)
                                val cbis = Connexion(nfin, ndepart!!)
                                if(ga.getConnexion(c) == null && ga.getConnexion(cbis) == null){
                                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                                    val input = EditText(this@MainActivity)
                                    alertDialog.setTitle("Etiquette de la connexion")
                                    alertDialog.setMessage("Entrez le nom de l'etiquette")
                                    alertDialog.setView(input)
                                    alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                                        //methode du bouton Valider
                                        val valsaisie = input.text.toString()
                                        if(valsaisie != ""){
                                            c.setetiquette(valsaisie)
                                            ga.addConnexion(c)
                                            img.setImageDrawable(DrawableGraph(ga))
                                            dialog.dismiss()
                                        }else{
                                            Toast.makeText(this,"Veuillez entrez un nom pour l'etiquette",Toast.LENGTH_LONG).show()
                                        }

                                    }
                                    alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    alertDialog.show()
                                }else{
                                    Toast.makeText(this,"Impossible de créer plusieurs connexions entre deux noeuds",Toast.LENGTH_LONG).show()
                                }
                            }
                            Log.i("", "Up: $upx - $upy")
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mx = event.x
                            my = event.y
                            ntp = Node(mx, my, "")
                            if (ndepart != null) {
                                ga.tmpConnexion = Connexion(ndepart!!, ntp!!)
                                //img.invalidate()
                                img.setImageDrawable(DrawableGraph(ga))
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
                            if (time1 >= ViewConfiguration.getLongPressTimeout()) {
                                xP = event.x
                                yP = event.y
                                if((xP>=30F && xP<=imgWidth-30F) && (yP>=30F && yP<=imgHeight-30F) ){
                                //Création de la boite de dialogue et de ses actions
                                    createNodeDialog()
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
                var time: Long = 0
                img.setOnTouchListener { _, event ->

                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            time = System.currentTimeMillis()
                        }
                        MotionEvent.ACTION_UP -> {
                            val time1 = System.currentTimeMillis() - time
                            if (time1 >= ViewConfiguration.getLongPressTimeout()) {
                                xP = event.x
                                yP = event.y
                                Log.i("","yes")
                                if(ga.getNode(xP,yP) != null){
                                    val d:nodeUpdateDialog = nodeUpdateDialog(this,this.ga,ga.getNode(xP,yP)!!)
                                    d.show()
                                }else if(ga.getConnexion(xP,yP) != null){
                                    val cd:ConnexionUpdateDialog = ConnexionUpdateDialog(this,this.ga,ga.getConnexion(xP,yP)!!)
                                    cd.show()
                                }
                            }
                        }
                    }
                    true
                }
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
                ga.reset()
                img.setImageDrawable(DrawableGraph(ga))
            }
            States.SAVE -> {
                this.title = resources.getString(R.string.app_name)
                if(ga.getTitre() != ""){
                    saveintofile(ga.getTitre())
                    Toast.makeText(this,resources.getString(R.string.dialoggraph_msg),Toast.LENGTH_LONG).show()
                }
            }
            States.IMPORT_NETWORK -> {
                this.title = resources.getString(R.string.app_name)
                var name1:String = ""
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                val input = EditText(this@MainActivity)
                alertDialog.setTitle(resources.getString(R.string.graph_title))
                alertDialog.setMessage(resources.getString(R.string.dialoggraph_text))
                alertDialog.setView(input)
                alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                    if(input.text!=null){
                        try {
                            ga = gettofile(input.text.toString())!!
                            img.invalidate()
                            img.setImageDrawable(DrawableGraph(ga))
                        }catch (e:IOException){
                            e.printStackTrace()
                            Toast.makeText(this,resources.getString(R.string.import_graph_msg),Toast.LENGTH_LONG).show()
                        }
                        dialog.dismiss()

                    }
                }
                alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()

            }
            States.IMPORT_PLAN -> {
                this.title = resources.getString(R.string.app_name)
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }
            States.SEND_NETWORK -> {
                this.title = resources.getString(R.string.app_name)
                screenshot()
            }
        }
    }

    /** Fonction pour sauvegarder un graphe **/
    @Throws(IOException::class)
    fun saveintofile(n:String) {
        val path:String = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()+"/"+n
        if(n.isNotEmpty()){
            var myf:File = File(path)
            val outputStream: FileOutputStream = FileOutputStream(myf)
            //var myout: OutputStreamWriter = OutputStreamWriter(outputStream)
            val json = Json.encodeToString(ga)
            //myout.append
            //outputStream.w
           // myout.close()
             outputStream.write(json.toByteArray())
             outputStream.flush()
             outputStream.close()
            Toast.makeText(this,"Graphe"+ga.getTitre()+"sauvegardé avec succés",Toast.LENGTH_LONG).show()
        }

    }
    /** Fonction pour recuperer un ancien graphe enregistré **/
    @Throws(IOException::class)
    fun gettofile(n:String): Graph? {
        if(n.isNotEmpty()){
            val path:String = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()+"/"+n
            var myf:File = File(path)
            var value: String? = null
            val inputStream: FileInputStream = FileInputStream(myf)
            return run {
                val stringb = StringBuilder()
                var content: Int
                while (inputStream.read().also { content = it } != -1) {
                    value = stringb.append(content.toChar()).toString()
                }
                Json.decodeFromString("$value")
            }
        }else{
            return null
        }

    }
    /** Boite de dialogue pour la creation de noeud **/
    fun createNodeDialog(){
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        val input = EditText(this@MainActivity)
        alertDialog.setTitle(resources.getString(R.string.noeud_etiquette))
        alertDialog.setMessage(resources.getString(R.string.dialognode_text))
        alertDialog.setView(input)
        alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
            //methode du bouton Valider
            val valsaisie = input.text.toString()
            if(ga.getNode(xP,yP) == null){
                ga.addNode(Node(xP, yP, valsaisie))
                //img.invalidate()
                img.setImageDrawable(DrawableGraph(ga))

            }else{
                Toast.makeText(this,resources.getString(R.string.dialognode_msg),Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }
        alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
    /** Boite de dialogue pour la creation de noeud **/
    fun createConnexionDialog(c:Connexion){
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        val input = EditText(this@MainActivity)
        alertDialog.setTitle("Etiquette de la connexion")
        alertDialog.setMessage("Entrez le nom de l'etiquette")
        alertDialog.setView(input)
        alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
            //methode du bouton Valider
            val valsaisie = input.text.toString()
            if(valsaisie != ""){
                c.setetiquette(valsaisie)
                dialog.dismiss()
            }else{
                Toast.makeText(this,"Veuillez entrez un nom pour l'etiquette",Toast.LENGTH_LONG).show()
            }

        }
        alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
    /** Boite de dialogue pour recuperer le titre du graphe  */
    @SuppressLint("StringFormatInvalid")
    fun GraphTitreDialog(){
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        val input = EditText(this@MainActivity)
        alertDialog.setTitle(resources.getString(R.string.graph_title))
        alertDialog.setMessage(resources.getString(R.string.dialoggraph_text))
        alertDialog.setView(input)
        alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
            if(input.text!=null){
                ga.setTitre(input.text.toString())
                graphTitre.setText(resources.getString(R.string.graph_name_text) +" "+ga.getTitre())
                graphTitre.invalidate()
                dialog.dismiss()
            }
        }
        alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
    /** Implementer pour telecharger des images et d'autres trucs du genre **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                val bg = Drawable.createFromStream(inputStream, imageUri.toString())
                img.background = bg
            } catch (e: FileNotFoundException) {
                  e.message
                  Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }
    /** faire un screenshot du réseau actuel **/
    fun screenshot(){
        val dirPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS).toString() + "/Network_DP"
        } else {
            TODO("VERSION.SDK_INT < Q")
        }
        val file = File(dirPath)

        if (!file.exists()) {
            val mkdirs = file.mkdirs()
        }

        val path = "$dirPath/${this.ga.getTitre()}.jpeg"
        /** Faire la capture d'ecran **/
        val bitmap = Bitmap.createBitmap(
            imgWidth.toInt(),
            imgHeight.toInt(), Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        img.draw(canvas)
        /** La stocker dans un dossier **/
        val image = File(path)
        try {
            val fileOutputStream = FileOutputStream(image)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        /** lancer l'intention de partage **/
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "image/jpeg"
        val email_destinataire = arrayOf("arnauld-cyriaque.djedjemel@etudiant.univ-rennes1.fr")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email_destinataire)
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name)+" "+resources.getString(R.string.email_subject)+"-"+this.ga.getTitre())
    }
    /** Pour sauvegarder l'etat lors de la rotation **/
    override fun onSaveInstanceState(outState: Bundle) {

        val json = Json.encodeToString(ga)
        val json2 = Json.encodeToString(mode)
        outState.putSerializable("graph",json)
        outState.putSerializable("mode",json2)
        super.onSaveInstanceState(outState)
    }
    /** Menu contextuel **/
    /*override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflate:MenuInflater = menuInflater
        inflate.inflate(R.menu.updatenode,menu)
    }*/

    /*override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteNode -> {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Message de confirmation")
                alertDialog.setMessage("Voulez vous vraiment supprimer cet objet?")
                alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                    /*if(this.ga.deleteNode(n)){
                        Toast.makeText(this,"Suppression réussie",Toast.LENGTH_LONG).show()
                    }*/
                }
                alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
            R.id.changeNodeLabel -> {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                val input = EditText(this@MainActivity)
                alertDialog.setTitle(resources.getString(R.string.noeud_etiquette))
                alertDialog.setMessage(resources.getString(R.string.dialognode_text))
                alertDialog.setView(input)
                alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                   /* if(input.text!=null){
                        n.setTitre(input.text.toString())
                    }*/
                    dialog.dismiss()
                }
                alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
            R.id.changeNodeColor -> {

            }
        }
        return super.onContextItemSelected(item)
    }*/
    fun updateNodepopUp(v:View,n:Node){
        var popup:PopupMenu = PopupMenu(this,v,Gravity.CENTER)
        popup.inflate(R.menu.updatenode)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.deleteNode -> {
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    alertDialog.setTitle("Message de confirmation")
                    alertDialog.setMessage("Voulez vous vraiment supprimer cet objet?")
                    alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                         if(this.ga.deleteNode(n)){
                             Toast.makeText(this,"Suppression réussie",Toast.LENGTH_LONG).show()
                         }
                    }
                    alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.show()
                }
                R.id.changeNodeLabel -> {
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                    val input = EditText(this@MainActivity)
                    alertDialog.setTitle(resources.getString(R.string.noeud_etiquette))
                    alertDialog.setMessage(resources.getString(R.string.dialognode_text))
                    alertDialog.setView(input)
                    alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                        if(input.text!=null){
                            n.setTitre(input.text.toString())
                        }
                        dialog.dismiss()
                    }
                    alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialog.show()
                }
                R.id.changeNodeColor -> {

                }
            }

            true
        })
        popup.show()
    }

}
