package msi.crool.sqlite

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import msi.crool.sqlite.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val db = DataBaseHandler(this)

        binding?.btnInsert?.setOnClickListener {
            if (binding?.textfield1?.text?.isNotEmpty() == true && binding?.textfield2?.text?.isNotEmpty() == true) {
                val user = User(
                    binding?.textfield1?.text.toString(),
                    binding?.textfield2?.text.toString().toInt()
                )
                db.insertData(user)
            } else {
                Toast.makeText(this, "Please Fill Data", Toast.LENGTH_LONG).show()
            }
        }

        binding?.btnRead?.setOnClickListener {
            val data = db.readData()
            binding?.result?.text = ""
            for (i in data.indices) {
                binding?.result?.append("${data[i].id} ${data[i].name} ${data[i].age}\n")
            }
        }

        binding?.btnDelete?.setOnClickListener {
            val idText = binding?.textfieldId?.text.toString()
            if (idText.isNotEmpty()) {
                val id = idText.toIntOrNull()
                if (id != null) {
                    db.deleteData(id)
                    Toast.makeText(this, "Record Deleted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Invalid ID input", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter ID to delete", Toast.LENGTH_LONG).show()
            }
        }

        binding?.btnUpdate?.setOnClickListener {
            val idText = binding?.textfieldId?.text.toString()
            if (idText.isNotEmpty()) {
                val id = idText.toIntOrNull()
                if (id != null) {
                    db.updateData(id)
                } else {
                    Toast.makeText(this, "Invalid ID input", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter ID to update", Toast.LENGTH_LONG).show()
            }
        }
    }
}
