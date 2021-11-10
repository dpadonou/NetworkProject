package fr.istic.mob.networkdp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

class ConnexionUpdateDialog: Dialog {
    private lateinit var ga:Graph
    private lateinit var c:Connexion
    private lateinit var m:MainActivity
    constructor(m:MainActivity,ga:Graph,c:Connexion) : super(m) {
        this.m = m
        this.ga = ga
        this.c = c
    }
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_connexion)
        /** Supprimez la connexion **/
        findViewById<Button>(R.id.btn_delete_connex).setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.context)
            alertDialog.setTitle(this.context.getString(R.string.confirm_text))
            alertDialog.setMessage(context.getString(R.string.confirm_drop_connexion))
            alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
                if(this.ga.deleteConnexion(this.c)){
                    Toast.makeText(this.context,context.getString(R.string.drop_success_text), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    this.m.getImg().invalidate()
                    this.m.getImg().setImageDrawable(DrawableGraph(this.ga))
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
        /** Modifier l'etiquette de la connexion **/
        findViewById<Button>(R.id.btn_connex_etiq_update).setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.m)
            val input = EditText(this.m)
            alertDialog.setTitle(context.getString(R.string.connexion_label_text))
            alertDialog.setMessage(context.getString(R.string.connexion_label))
            alertDialog.setView(input)
            alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
                //methode du bouton Valider
                val valsaisie = input.text.toString()
                if( input.text!=null){
                    this.c.setetiquette(input.text.toString())
                    this.m.getImg().invalidate()
                    //this.m.getimg().setImageDrawable(DrawableGraph(this.ga))
                    this.dismiss()
                }else{
                    Toast.makeText(this.m,context.getString(R.string.etiquette_forget_text),Toast.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }
            alertDialog.setNegativeButton(this.m.resources.getString(R.string.annuler_text)) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
        /** Modifiez la couleur de la connexion **/
        findViewById<Button>(R.id.btn_modify_connexColor).setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.m)
            alertDialog.setTitle(context.getString(R.string.connexion_color_title))
            alertDialog.setMessage(context.getString(R.string.color_text))
            val inflater: LayoutInflater = this.layoutInflater
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
                    R.id.noirradio -> check= Color.BLACK
                }
            }
            alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
                if(check != 0){
                    this.ga.getConnexion(c)!!.setcouleur(check)
                    this.m.getImg().invalidate()
                    //this.m.getimg().setImageDrawable(DrawableGraph(this.ga))
                    this.dismiss()
                }
                dialog.dismiss()
            }
            alertDialog.setNegativeButton(this.m.resources.getString(R.string.annuler_text)) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
        /** Modifiez l'epaisseur de la connexion **/
        findViewById<Button>(R.id.btn_modifiy_connexWidth).setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.m)
            alertDialog.setTitle(context.getString(R.string.connexion_width_title))
            alertDialog.setMessage(context.getString(R.string.connexion_width_text))
            val inflater: LayoutInflater = this.layoutInflater
            val dv: View = inflater.inflate(R.layout.layout_connexion_width, null)
            alertDialog.setView(dv)
            val rg2 = dv.findViewById<RadioGroup>(R.id.rgWidth)
            var check:Float = 0F
            rg2.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.btn_connex_small -> check=10F
                    R.id.btn_connex_medium -> check=20F
                    R.id.btn_connex_large -> check=40F
                }
            }
            alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
                if(check != 0F){
                    this.c.setEpaisseur(check)
                    this.m.getImg().invalidate()
                   // this.m.getimg().setImageDrawable(DrawableGraph(this.ga))
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