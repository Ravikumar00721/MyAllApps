package com.example.realimedatabase.Activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.realimedatabase.Model.EmpModel
import com.example.realimedatabase.databinding.ActivityDetailsBinding
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValuesToViews()

        binding.btnUpdate.setOnClickListener {
            val empId=binding.etID.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val age = binding.etAge.text.toString().trim()
            val salary = binding.etSalary.text.toString().trim()

            updateData(empId,name, age, salary)
        }
        binding.btnDelete.setOnClickListener {
            deleteData()
        }
    }

    private fun updateData(empid:String, name: String, age: String, salary: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(empid)
        val empInfo = EmpModel(empid, name, age, salary)
        dbRef.setValue(empInfo)
    }

    private fun deleteData() {
        val empId=binding.etID.text.toString().trim()
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(empId)
        dbRef.removeValue()
    }

    private fun setValuesToViews() {
        binding.etID.setText(intent.getStringExtra("empId"))
        binding.etName.setText(intent.getStringExtra("name"))
        binding.etAge.setText(intent.getStringExtra("age"))
        binding.etSalary.setText(intent.getStringExtra("salary"))
    }
}
