package com.example.realimedatabase.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.realimedatabase.Model.EmpModel
import com.example.realimedatabase.databinding.InsertActivityBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class activity_insert : AppCompatActivity() {
    lateinit var binding: InsertActivityBinding
    lateinit var databaseRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=InsertActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseRef=FirebaseDatabase.getInstance().getReference("Employees")

        binding.save.setOnClickListener {
            saveEmpData()
        }
    }

    private fun saveEmpData() {
        val Name=binding.name.text.toString()
        val Age=binding.age.text.toString()
        val Salary=binding.salary.text.toString()

        if(Name.isEmpty())
        {
            Toast.makeText(this,"Pls Enter Name",Toast.LENGTH_SHORT).show()
        }
        if(Age.isEmpty())
        {
            Toast.makeText(this,"Pls Enter Age",Toast.LENGTH_SHORT).show()
        }
        if(Salary.isEmpty())
        {
            Toast.makeText(this,"Pls Enter Salary",Toast.LENGTH_SHORT).show()
        }
       val empId=databaseRef.push().key!!

        val employee=EmpModel(empId,Name,Age,Salary)

        databaseRef.child(empId).setValue(employee)
            .addOnSuccessListener {
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                binding.name.text.clear()
                binding.age.text.clear()
                binding.salary.text.clear()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            }
    }
}