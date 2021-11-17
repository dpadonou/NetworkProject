package fr.istic.mob.networkDP

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity() {
    private lateinit var ga: Graph
    private var xP: Float = 0F
    private var yP: Float = 0F
    private var mode: States = States.RESET
    private lateinit var img: ImageView
    private lateinit var graphTitre: TextView
    private var downX: Float = 0F
    private var downY: Float = 0F
    private var upX: Float = 0F
    private var upY: Float = 0F
    private var mX: Float = 0F
    private var mY: Float = 0F
    private var imgHeight: Float = 0F
    private var imgWidth: Float = 0F
    private val pickImage = 100
    private var imageUri: Uri? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        graphTitre = findViewById(R.id.graphTitre)
        img = findViewById(R.id.imageView)
        img.background = getDrawable(R.drawable.plan_)
        ga = Graph()

        img.viewTreeObserver.addOnGlobalLayoutListener {
            imgWidth = img.measuredWidth.toFloat()
            imgHeight = img.measuredHeight.toFloat()
        }
        initAfterRotation(savedInstanceState)
    }
    /** Initialise l'application en cas de rotation de l'ecran **/
    private fun initAfterRotation(savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            val json = savedInstanceState.getSerializable("graph")
            val json2 = savedInstanceState.getSerializable("mode")
            ga = Json.decodeFromString("$json")
            mode = Json.decodeFromString("$json2")
            (resources.getString(R.string.graph_name_text) + " " + ga.getTitre()).also { graphTitre.text = it }
            remakeGraph(ga)
            img.setImageDrawable(DrawableGraph(ga))
            img.invalidate()
            doSomething(mode)
        }else{
            graphTitreDialog()
            Toast.makeText(this,resources.getString(R.string.dialoggraph_msg),Toast.LENGTH_LONG).show()
            img.setImageDrawable(DrawableGraph(ga))
        }
    }

    /** Retourne une instance de l'ImageView **/
     fun getImg(): ImageView {
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
                doSomething(mode)
            }
            //Lorsqu'on clique sur Modifier objet
            R.id.reading_mode -> {
                mode = States.READING_MODE
                doSomething(mode)
            }
            //Lorsqu'on clique sur ajouter Connexion
            R.id.add_connect -> {
                mode = States.ADDING_CONNEXION
                doSomething(mode)
            }
            //Lorsqu'on clique sur modifier connexion
            R.id.update -> {
                mode = States.UPDATE_MODE
                doSomething(mode)
            }
            //Lorsqu'on clique sur renitialiser le graphe
            R.id.reset -> {
                mode = States.RESET
                doSomething(mode)
            }
            //Lorsqu'on clique sur Sauvegarder le graphe
            R.id.save -> {
                mode = States.SAVE
                doSomething(mode)
            }
            //Lorsqu'on clique sur importer un graphe
            R.id.import_graph -> {
                mode = States.IMPORT_NETWORK
                doSomething(mode)
            }
            //Lorsqu'on clique sur importer de nouveau plan
            R.id.import_plan -> {
                mode = States.IMPORT_PLAN
                doSomething(mode)
            }
            //Lorsqu'on clique sur envoyer la capture du graphe
            R.id.send_network -> {
                mode = States.SEND_NETWORK
                doSomething(mode)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Gerer les modes **/
    private fun doSomething(m: States) {
        when (m) {
            States.ADDING_CONNEXION -> {
                addConnexion()
            }
            States.ADDING_NODE -> {
                addNode()
            }
            States.UPDATE_MODE -> {
                enableUpdate()
            }
            States.READING_MODE -> {
                enableReadMode()
            }
            States.RESET -> {
                img.parent.requestDisallowInterceptTouchEvent(false)
                reset()
            }
            States.SAVE -> {
                img.parent.requestDisallowInterceptTouchEvent(false)
                save()
            }
            States.IMPORT_NETWORK -> {
                img.parent.requestDisallowInterceptTouchEvent(false)
                importNetwork()
            }
            States.IMPORT_PLAN -> {
                img.parent.requestDisallowInterceptTouchEvent(false)
                importPlan()
            }
            States.SEND_NETWORK -> {
                sendNetworkByMail()
            }
        }
    }

    /** Fonction pour sauvegarder un graphe **/
    @Throws(IOException::class)
    private fun saveIntoFile(n: String) {
        val path: String = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + n
        if (n.isNotEmpty()) {
            val myf = File(path)
            val outputStream = FileOutputStream(myf)
            val json = Json.encodeToString(ga)
            outputStream.write(json.toByteArray())
            outputStream.flush()
            outputStream.close()
        }
    }

    /** Fonction pour recuperer un ancien graphe enregistré **/
    @Throws(IOException::class)
    private fun getFromFile(n: String): Graph? {
        if (n.isNotEmpty()) {
            val path: String =
                getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + n
            val myf = File(path)
            var value: String? = null
            val inputStream = FileInputStream(myf)
            return run {
                val stringb = StringBuilder()
                var content: Int
                while (inputStream.read().also { content = it } != -1) {
                    value = stringb.append(content.toChar()).toString()
                }
                Json.decodeFromString("$value")
            }
        } else {
            return null
        }
    }

    /** Boite de dialogue pour la creation de noeud **/
    private fun createNodeDialog() {
        if (this.ga.getTitre().isEmpty()) {
            graphTitreDialog()
        } else {
            val alertDialog = AlertDialog.Builder(this@MainActivity)
            alertDialog.setCancelable(false)
            val input = EditText(this@MainActivity)
            alertDialog.setTitle(resources.getString(R.string.noeud_etiquette))
            alertDialog.setMessage(resources.getString(R.string.dialognode_text))
            alertDialog.setView(input)
            alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
                //methode du bouton Valider
                val valsaisie = input.text.toString()
                if(valsaisie != ""){
                    if (ga.getNode(xP, yP) == null) {
                        ga.addNode(Node(xP, yP, valsaisie))
                        img.setImageDrawable(DrawableGraph(ga))
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.dialognode_msg),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                dialog.dismiss()
            }
            alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
    }

    /** Boite de dialogue pour recuperer le titre du graphe  */
    private fun graphTitreDialog() {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setCancelable(false)
        val input = EditText(this@MainActivity)
        alertDialog.setTitle(resources.getString(R.string.graph_title))
        alertDialog.setMessage(resources.getString(R.string.dialoggraph_msg)+"\n"+ resources.getString(R.string.dialoggraph_text))
        alertDialog.setView(input)
        alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
            if (input.text != null) {
                ga.setTitre(input.text.toString())
                (resources.getString(R.string.graph_name_text) + " " + ga.getTitre()).also { graphTitre.text = it }
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
                Toast.makeText(this, getString(R.string.import_plan_success), Toast.LENGTH_LONG)
                    .show()
            } catch (e: FileNotFoundException) {
                e.message
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    /** effectuerMode un screenshot du réseau actuel **/
    private fun screenShot(): Intent {
        val dirPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS).toString() + "/Network_DP"
        } else {
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/Network_DP"
        }
        val file = File(dirPath)

        if (!file.exists()) {
            file.mkdirs()
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
        emailIntent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(
                this,
                this.applicationContext.packageName + ".provider",
                image
            )
        )
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            resources.getString(R.string.app_name) + " " + resources.getString(R.string.email_subject) + "-" + this.ga.getTitre()
        )
        return emailIntent
    }

    /** Bouge les noeuds selon les mouvements de l'utilisateur **/
    private fun enableReadMode() {
        val sb = StringBuilder()
        sb.append(resources.getString(R.string.app_name))
            .append(" - " + resources.getString(R.string.reading_text))
        this.title = sb.toString()
        var selectNode: Node? = null
        var selectedConnex: Connexion? = null
        img.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    if (ga.getNode(downX, downY) != null) {
                        selectNode = ga.getNode(downX, downY)
                    } else if (ga.getGraphConnexionByMiddlePosition(downX,downY) != null) {
                        selectedConnex = ga.getGraphConnexionByMiddlePosition(downX,downY)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    selectNode = null
                    selectedConnex = null
                   img.parent.requestDisallowInterceptTouchEvent(false)
                }
                MotionEvent.ACTION_MOVE -> {
                    img.parent.requestDisallowInterceptTouchEvent(true)
                    mX = event.x
                    mY = event.y
                    if (selectNode != null) {
                        if ((mX >= 30F && mX <= imgWidth - 30F) && (mY >= 30F && mY <= imgHeight - 30F)) {
                            selectNode!!.setPosY(mY)
                            selectNode!!.setPosX(mX)
                            img.setImageDrawable(DrawableGraph(ga))
                        }
                    } else if (selectedConnex != null) {
                        selectedConnex!!.isCurved = true
                        selectedConnex!!.mX = mX
                        selectedConnex!!.mY = mY
                        img.setImageDrawable(DrawableGraph(ga))
                    }
                }
            }
            true
        }
    }

    /** Renitialise le graphe **/
    private fun reset() {
        this.title = resources.getString(R.string.app_name)
        ga.reset()
        img.setImageDrawable(DrawableGraph(ga))
    }

   /** Sauvegarder le graphe dans la mémoire interne **/
    private fun save() {
        this.title = resources.getString(R.string.app_name)
        if (ga.getTitre() != "") {
            Toast.makeText(this, resources.getString(R.string.dialoggraph_msg), Toast.LENGTH_LONG)
                .show()
            saveIntoFile(ga.getTitre())
            Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_LONG).show()
        }
    }

   /** Importe un graphe sauvegardé et l'affiche **/
    private fun importNetwork() {
        this.title = resources.getString(R.string.app_name)
        val alertDialog = AlertDialog.Builder(this@MainActivity)
       alertDialog.setCancelable(false)
        val input = EditText(this@MainActivity)
        alertDialog.setTitle(resources.getString(R.string.graph_title))
        alertDialog.setMessage(resources.getString(R.string.dialoggraph_text))
        alertDialog.setView(input)
        alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
            if (input.text != null) {
                try {
                    ga = getFromFile(input.text.toString())!!
                    remakeGraph(ga)
                    img.invalidate()
                    img.setImageDrawable(DrawableGraph(ga))
                    Toast.makeText(this, getString(R.string.import_success), Toast.LENGTH_LONG)
                        .show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        resources.getString(R.string.import_graph_msg),
                        Toast.LENGTH_LONG
                    ).show()
                }
                dialog.dismiss()
            }
        }
        alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    /** Importe une nouvelle image de plan **/
    private fun importPlan() {
        this.title = resources.getString(R.string.app_name)
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    /**Envoi une capture du graphe par mail **/
    private fun sendNetworkByMail() {
        this.title = resources.getString(R.string.app_name)
        img.parent.requestDisallowInterceptTouchEvent(false)
        val f = screenShot()
        this.startActivity(f)
    }

    /** Ajoute un nouveau noeud au graphe **/
    private fun addNode() {
        val sb = StringBuilder()
        sb.append(resources.getString(R.string.app_name)).append(" - "+ resources.getString(R.string.add_object_text))
        this.title = sb.toString()
        var time: Long = 0
        img.parent.requestDisallowInterceptTouchEvent(false)
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
                            Toast.makeText(this,getString(R.string.node_creation_error),Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
            true
        }

    }

    /**Ajoute une nouvelle connexion au graphe **/
    private fun addConnexion() {
        val sb = StringBuilder()
        sb.append(resources.getString(R.string.app_name))
            .append(" - "+ resources.getString(R.string.add_connect_text))
        this.title = sb.toString()
        var ndepart: Node? = null
        var ntp: Node?
        lateinit var nfin: Node
        //img.parent.requestDisallowInterceptTouchEvent(false)
        img.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    ndepart = ga.getNode(downX, downY)
                    Log.i("", "Down : ${ndepart.toString()}")
                }
                MotionEvent.ACTION_UP -> {
                    upX = event.x
                    upY = event.y
                    val nfintp: Node? = ga.getNode(upX, upY)
                    if (nfintp != null && ndepart !=null) {
                        if(nfintp != ndepart){
                            nfin = nfintp
                            val c = Connexion(ndepart!!, nfin)
                            val cbis = Connexion(nfin, ndepart!!)
                            if(ga.getGraphConnexion(c) == null && ga.getGraphConnexion(cbis) == null){
                                  createConnectionDialog(c)
                            }else{
                                Toast.makeText(this,getString(R.string.connexion_creation_error),Toast.LENGTH_LONG).show()
                            }
                        }else {
                            ga.settmpConnexion(null)
                            img.invalidate()
                        }

                    }else{
                        ga.settmpConnexion(null)
                        img.invalidate()
                    }
                    img.parent.requestDisallowInterceptTouchEvent(false)
                }
                MotionEvent.ACTION_MOVE -> {
                   img.parent.requestDisallowInterceptTouchEvent(true)
                    mX = event.x
                    mY = event.y
                    ntp = Node(mX, mY, "")
                    if (ndepart != null) {
                        ga.settmpConnexion(Connexion(ndepart!!, ntp!!))
                        img.invalidate()
                    }
                }
            }
            true
        }

    }

    /** Affiche une dialogue pour saisir le nom de la connexion
     * Crée la connexion une fois son etiquette validée
     **/
    private fun createConnectionDialog(connexion:Connexion) {
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setCancelable(false)
        val input = EditText(this@MainActivity)
        alertDialog.setTitle(resources.getString(R.string.connexion_label_text))
        alertDialog.setMessage(resources.getString(R.string.connexion_label))
        alertDialog.setView(input)
        alertDialog.setPositiveButton(resources.getString(R.string.valider_text)) { dialog, _ ->
            //methode du bouton Valider
            val valsaisie = input.text.toString()
            if(valsaisie != ""){
                connexion.setTag(valsaisie)
                ga.addConnexion(connexion)
                ga.settmpConnexion(null)
                img.invalidate()
                dialog.dismiss()
            }else{
                Toast.makeText(this,resources.getString(R.string.etiquette_forget_text),Toast.LENGTH_LONG).show()
            }

        }
        alertDialog.setNegativeButton(resources.getString(R.string.annuler_text)) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    /**Modifie soit les connexions, soit les noeuds du graphe **/
    private fun enableUpdate() {
        val sb = StringBuilder()
        sb.append(resources.getString(R.string.app_name)).append(" - "+ resources.getString(R.string.update))
        this.title = sb.toString()
        var time: Long = 0
       // img.parent.requestDisallowInterceptTouchEvent(false)
        img.setOnTouchListener { _, event ->
            //img.parent.requestDisallowInterceptTouchEvent(true)
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
                            val d = NodeUpdateDialog(this,this.ga,ga.getNode(xP,yP)!!)
                            d.show()
                        }else if(ga.getGraphConnexionByMiddlePosition(xP,yP) != null){
                            val cd = ConnexionUpdateDialog(this,this.ga,ga.getGraphConnexionByMiddlePosition(xP,yP)!!)
                            cd.show()
                        }
                    }
                }
            }
            true
        }

    }

    /** Pour sauvegarder l'etat lors de la rotation **/
    override fun onSaveInstanceState(outState: Bundle) {
        val json = Json.encodeToString(ga)
        val json2 = Json.encodeToString(mode)
        // val d = Json.encodeToString(this.img.background)
        outState.putSerializable("graph", json)
        outState.putSerializable("mode", json2)
        //outState.putSerializable("drawable",d)
        super.onSaveInstanceState(outState)
    }

    private fun remakeGraph(graph:Graph){
        if(graph.getConnexionList().isNotEmpty()){
            for(connexion in graph.getConnexionList()){
                for(node in graph.getNodeList()){
                    if(connexion.getEmitter().getPosX() == node.getPosX() && connexion.getEmitter().getPosY() == node.getPosY()){
                       connexion.setEmitter(node)
                    }else if(connexion.getReceiver().getPosX() == node.getPosX() && connexion.getReceiver().getPosY() == node.getPosY()){
                        connexion.setReceiver(node)
                    }
                }
            }
        }
    }

}
