package fr.istic.mob.networkdp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*

class nodeUpdateDialog : Dialog {
    private lateinit var ga:Graph
    private lateinit var n:Node
    private lateinit var m:MainActivity
    constructor(m:MainActivity,ga:Graph,n:Node) : super(m) {
        this.m = m
        this.ga = ga
        this.n = n
    }
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_node)
        /** Supprimez le noeud **/
        findViewById<Button>(R.id.btn_delete_connex).setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.context)
            alertDialog.setTitle(context.getString(R.string.confirm_text))
            alertDialog.setMessage(context.getString(R.string.confim_drop_node))
            alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
                if(this.ga.deleteNode(this.n)){
                    Toast.makeText(this.context,context.getString(R.string.drop_success_text), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    this.m.getimg().invalidate()
                    this.m.getimg().setImageDrawable(DrawableGraph(this.ga))
                    this.dismiss()
                }else{
                    dialog.dismiss()
                }
            }
            alertDialog.setNegativeButton(this.context.resources.getString(R.string.annuler_text)) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
        /** Modifier l'etiquette du noeud **/
        findViewById<Button>(R.id.btn_connex_etiq_update).setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.m)
            val input = EditText(this.m)
            alertDialog.setTitle(this.m.resources.getString(R.string.noeud_etiquette))
            alertDialog.setMessage(this.m.resources.getString(R.string.dialognode_text))
            alertDialog.setView(input)
            alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
                //methode du bouton Valider
                val valsaisie = input.text.toString()
                if( input.text!=null){
                    this.n.setTitre(input.text.toString())
                    this.m.getimg().invalidate()
                    this.m.getimg().setImageDrawable(DrawableGraph(this.ga))
                    this.dismiss()
                }else{
                    Toast.makeText(this.m,context.getString(R.string.node_etiquette_modify),Toast.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
            alertDialog.setNegativeButton(this.m.resources.getString(R.string.annuler_text)) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
        /** modifiez la couleur **/
        findViewById<Button>(R.id.btn_modify_connexColor).setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.m)
            alertDialog.setTitle(context.getString(R.string.node_color_title))
            alertDialog.setMessage(context.getString(R.string.color_text))
             val inflater:LayoutInflater = this.layoutInflater
            val dv: View = inflater.inflate(R.layout.layout_color_node, null)
            alertDialog.setView(dv)
            val rg = dv.findViewById<RadioGroup>(R.id.rgcolor)
            var check:Int = 0
            rg.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.cyanradio -> check = Color.rgb(0, 255, 255)
                    R.id.redradio -> check = Color.RED
                    R.id.greenradio -> check = Color.GREEN
                    R.id.blueradio -> check = Color.BLUE
                    R.id.orangeradio -> check = Color.rgb(255, 165, 0)
                    R.id.magentaradio -> check= Color.rgb(255, 0, 255)
                    R.id.noirradio -> check=Color.BLACK
                }
            }
            alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
                if(check != 0){
                    this.n.setcouleur(check)
                    this.m.getimg().invalidate()
                    this.m.getimg().setImageDrawable(DrawableGraph(this.ga))
                    this.dismiss()
                }
                dialog.dismiss()
            }
            alertDialog.setNegativeButton(this.m.resources.getString(R.string.annuler_text)) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
    }

}