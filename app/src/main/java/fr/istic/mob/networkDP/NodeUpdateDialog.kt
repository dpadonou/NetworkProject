package fr.istic.mob.networkDP

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

class NodeUpdateDialog(private var m: MainActivity, private var ga: Graph, private var n: Node) : Dialog(m) {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_node)
        /** Supprimez le noeud **/
        findViewById<Button>(R.id.btn_delete_connex).setOnClickListener {
            deleteNode(this.n)
        }

        /** Modifier l'etiquette du noeud **/
        findViewById<Button>(R.id.btn_connex_etiq_update).setOnClickListener {
            updateNodeLabel(this.n)
        }

        /** modifiez la couleur **/
        findViewById<Button>(R.id.btn_modify_connexColor).setOnClickListener {
            updateNodeColor(this.n)
        }
    }

    /**
     * Affiche une dialogue de confirmation
     *Supprime le noeud passé en paramètre si l'utilisateur confirme la suppression
     */
    private fun deleteNode(node:Node){
        val alertDialog = AlertDialog.Builder(this.context)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(context.getString(R.string.confirm_text))
        alertDialog.setMessage(context.getString(R.string.confim_drop_node))
        alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
            if (this.ga.deleteNode(node)) {
                Toast.makeText(
                    this.context,
                    context.getString(R.string.drop_success_text),
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
                this.m.getImg().invalidate()
                this.dismiss()
            } else {
                dialog.dismiss()
            }
        }
        alertDialog.setNegativeButton(this.context.resources.getString(R.string.annuler_text)) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
    /**
     * Affiche une dialogue pour entrer une nouvelle valeur d'etiquette
     * Modifie l'etiquette du noeud passé en paramètre
     */
    private fun updateNodeLabel(node:Node){
        val alertDialog = AlertDialog.Builder(this.m)
        alertDialog.setCancelable(false)
        val input = EditText(this.m)
        alertDialog.setTitle(this.m.resources.getString(R.string.noeud_etiquette))
        alertDialog.setMessage(this.m.resources.getString(R.string.dialognode_text))
        alertDialog.setView(input)
        alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
            //methode du bouton Valider
            if (input.text != null && input.text.toString() != "") {
                node.setTitre(input.text.toString())
                this.m.getImg().invalidate()
                this.dismiss()
            } else {
                Toast.makeText(
                    this.m,
                    context.getString(R.string.node_etiquette_modify),
                    Toast.LENGTH_LONG
                ).show()
            }
            dialog.dismiss()
        }
        alertDialog.setNegativeButton(this.m.resources.getString(R.string.annuler_text)) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    /**
     * Affiche une dialogue pour selectionner la couleur
     * Modifie la couleur du noeud passé en paramètre
     */
    private fun updateNodeColor(node:Node){
        val alertDialog = AlertDialog.Builder(this.m)
        alertDialog.setCancelable(false)
        alertDialog.setTitle(context.getString(R.string.node_color_title))
        alertDialog.setMessage(context.getString(R.string.color_text))
        val inflater: LayoutInflater = this.layoutInflater
        val dv: View = inflater.inflate(R.layout.layout_color_node, null)
        alertDialog.setView(dv)
        val rg = dv.findViewById<RadioGroup>(R.id.rgcolor)
        var check = 0
        rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.cyanradio -> check = Color.rgb(0, 255, 255)
                R.id.redradio -> check = Color.RED
                R.id.greenradio -> check = Color.GREEN
                R.id.blueradio -> check = Color.BLUE
                R.id.orangeradio -> check = Color.rgb(255, 165, 0)
                R.id.magentaradio -> check = Color.rgb(255, 0, 255)
                R.id.noirradio -> check = Color.BLACK
            }
        }

        alertDialog.setPositiveButton(this.m.resources.getString(R.string.valider_text)) { dialog, _ ->
            if (check != 0) {
                node.setcouleur(check)
                this.m.getImg().invalidate()
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