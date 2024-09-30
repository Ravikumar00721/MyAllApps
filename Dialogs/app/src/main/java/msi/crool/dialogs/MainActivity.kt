package msi.crool.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.snackbar.Snackbar // Import the correct Snackbar

class MainActivity : ComponentActivity() {
    private var butt: TextView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        butt = findViewById(R.id.snackbar_text)

        butt?.setOnClickListener { view ->
            showSnackbar(view)
        }
    }

    private fun showSnackbar(view: View) {
        Snackbar.make(view, "This is a Snackbar", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                // Handle the undo action
                Toast.makeText(this, "Undo clicked", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun alertDF() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Saavdhaan")
        builder.setMessage("Ispe Click Mat Kerna")
        builder.setIcon(R.drawable.baseline_warning_amber_24)

        builder.setPositiveButton("YES") { dialogInterface, _ ->
            Toast.makeText(this, "YES Clicked", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("NO") { dialogInterface, _ ->
            Toast.makeText(this, "NO Clicked", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }

        builder.setNeutralButton("Cancel") { dialogInterface, _ ->
            Toast.makeText(this, "Cancel Clicked", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
