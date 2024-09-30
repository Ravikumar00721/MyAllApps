package msi.crool.roomdatabase

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import msi.crool.roomdatabase.databinding.ActivityMainBinding
import msi.crool.roomdatabase.databinding.EditlayoutBinding
import msi.crool.roomdatabase.ui.theme.RoomDataBaseTheme

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDAO=(application as EmpApplication).db.employeeDao()
        binding?.btnSubmit?.setOnClickListener {
            addRecord(employeeDAO)
        }

        lifecycleScope.launch {
            employeeDAO.fetchAllEmployees().collect()
            {
                val list=ArrayList(it)
                setUpListOfDataInRecycleView(list,employeeDAO)
            }
        }

    }
    fun addRecord(employeeDAO: DAO)
    {
        val name=binding?.etName?.text.toString()
        val email=binding?.etEmail?.text.toString()

        if(name.isNotEmpty() && email.isNotEmpty())
        {
            lifecycleScope.launch {
                employeeDAO.insert(EmployeEntity(name=name, EmailID = email))
                Toast.makeText(this@MainActivity,"Record Saved",Toast.LENGTH_LONG).show()
                binding?.etName?.text?.clear()
                binding?.etEmail?.text?.clear()
            }
        }
        else{
            Toast.makeText(this,"Enter Values",Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpListOfDataInRecycleView(
        employeeList: ArrayList<EmployeEntity>,
        employeeDao: DAO
    ) {
        if (employeeList.isNotEmpty()) {
            val itemAdapter=ItemAdapter(employeeList,{
                updateId->
                updateRecordDialog(updateId,employeeDao)
            },{
                deleteID->
                deleteRecord(deleteID,employeeDao)
            })
            binding?.recyclerView?.layoutManager=LinearLayoutManager(this)
            binding?.recyclerView?.adapter=itemAdapter
            binding?.recyclerView?.visibility=View.VISIBLE
            binding?.inserted?.visibility=View.INVISIBLE
        }
        else {
            binding?.recyclerView?.visibility=View.INVISIBLE
            binding?.inserted?.visibility=View.VISIBLE

        }
    }

    private fun updateRecordDialog(id: Int, employeeDAO: DAO) {
        val updateDialog = Dialog(this, R.style.Theme_RoomDataBase)
        updateDialog.setCancelable(false)

        // Inflate and set the custom layout for the dialog
        val dialogBinding = EditlayoutBinding.inflate(layoutInflater)
        updateDialog.setContentView(dialogBinding.root)

        // Fetch the employee by ID and populate the dialog fields
        lifecycleScope.launch {
            employeeDAO.fetchEmployeeByID(id).collect { employee ->
                dialogBinding.editName.setText(employee.name)
                dialogBinding.editEmail.setText(employee.EmailID)
            }
        }


        // Set up the update button
        dialogBinding.buttonUpdate.setOnClickListener {
            val name = dialogBinding.editName.text.toString()
            val email = dialogBinding.editEmail.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    employeeDAO.update(EmployeEntity(id, name, email))
                    Toast.makeText(this@MainActivity, "Record Updated", Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(this, "Enter Values", Toast.LENGTH_LONG).show()
            }
        }

        // Set up the cancel button
        dialogBinding.buttonCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }


    private fun deleteRecord(id:Int,EmployeeDao:DAO)
    {
       val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("Delete")
        alertDialog.setPositiveButton("Yes")
        {
            dialog,_->
            run {
                lifecycleScope.launch {
                    EmployeeDao.delete(EmployeEntity(id))
                }
            }
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("NO")
        {
            dialog,_->run{
            dialog.dismiss()
            }
        }
        val ad:AlertDialog=alertDialog.create()
        ad.setCancelable(false)
        ad.show()
    }

}

