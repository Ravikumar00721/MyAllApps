package msi.crool.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import msi.crool.roomdemo.databinding.ActivityMainBinding
import msi.crool.roomdemo.databinding.DialogupdateBinding

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val employeDao=(application as EmployeeApp).db.employeDao()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.idButton?.setOnClickListener {
            addRecord(employeDao)
        }

        lifecycleScope.launch {
            employeDao.fetchAllEmployees().collect { employees ->
                Log.d("MainActivity", "Number of employees fetched: ${employees.size}")
                val list = ArrayList(employees)
                setUpListOfDataIntoRecycleView(list, employeDao)
            }
        }


    }
    fun addRecord(EmpDao:EmployeDAO)
    {
        val name=binding?.editText?.text.toString()
        val email=binding?.editMail?.text.toString()

        if(name.isNotEmpty() && email.isNotEmpty())
        {
            lifecycleScope.launch {
                EmpDao.insert(dataClass(name=name, email = email))
                Toast.makeText(this@MainActivity,"Record was Saved",Toast.LENGTH_LONG).show()
                binding?.editText?.text?.clear()
                binding?.editMail?.text?.clear()
            }
        }
        else
        {
            Toast.makeText(this@MainActivity,"Pls Enter Details",Toast.LENGTH_LONG).show()
        }
    }
    

    private fun setUpListOfDataIntoRecycleView(employeesList: ArrayList<dataClass>, employeDAO: EmployeDAO) {

        if (employeesList.isNotEmpty()) {
            val itemAdapter=ItemAdapter(employeesList,
                {
                    updateID->
                    updateRecord(updateID,employeDAO)
                },
                {
                    deleteID->
                    deleteRecord(deleteID,employeDAO)
                })

            binding?.rvItemList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemList?.adapter = itemAdapter
            binding?.rvItemList?.visibility = View.VISIBLE
            binding?.noRecords?.visibility = View.GONE
        }
        else
        {
            binding?.rvItemList?.visibility = View.GONE
            binding?.noRecords?.visibility = View.VISIBLE
        }
    }

    fun updateRecord(id: Int, employeDAO: EmployeDAO) {
        // Create and configure the dialog
        val updateDialog = Dialog(this, R.style.Theme_RoomDemo)
        updateDialog.setCanceledOnTouchOutside(false)
        val bind = DialogupdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(bind.root)

        // Fetch employee data by ID and populate the dialog
        lifecycleScope.launch {
            employeDAO.fetchAllEmployeesbyID(id).collect { employee ->
                if (employee.isNotEmpty()) {
                    // Use a method to set text if employee is found
                    setTextFields(bind, employee[0])
                }
            }
        }

        // Handle dialog actions (example: cancel button)
        bind.cancel.setOnClickListener {
            updateDialog.dismiss() // Dismiss the dialog when cancel button is clicked
        }

        bind.update.setOnClickListener {
            // Handle save button click
            val updatedName = bind.dEditName.text.toString()
            val updatedEmail = bind.dEditMail.text.toString()

            if (updatedName.isNotEmpty() && updatedEmail.isNotEmpty()) {
                lifecycleScope.launch {
                    employeDAO.update(dataClass(id = id, name = updatedName, email = updatedEmail))
                    Toast.makeText(this@MainActivity, "Record Updated", Toast.LENGTH_SHORT).show()
                    updateDialog.dismiss() // Dismiss the dialog after updating
                }
            } else {
                Toast.makeText(this@MainActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Show the dialog
        updateDialog.show()
    }


    private fun setTextFields(bind: DialogupdateBinding, employee: dataClass) {
        bind.dEditName.setText(employee.name)
        bind.dEditMail.setText(employee.email)
    }


    private fun deleteRecord(id:Int,employeDAO: EmployeDAO)
    {
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(R.drawable.baseline_delete_24)
        builder.setPositiveButton("YES")
        {dialoginterface,_->
            lifecycleScope.launch {
                employeDAO.delete(dataClass(id))
                Toast.makeText(this@MainActivity, "Record Deleted Successfully", Toast.LENGTH_SHORT).show()
            }

        }
        builder.setNegativeButton("No")
        {dialogInterface,_->
            dialogInterface.dismiss()
        }
        val alertDialog:AlertDialog=builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }
}

