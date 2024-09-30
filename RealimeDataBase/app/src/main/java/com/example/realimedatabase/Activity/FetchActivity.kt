package com.example.realimedatabase.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realimedatabase.Adapter.EmpAdapter
import com.example.realimedatabase.Model.EmpModel
import com.example.realimedatabase.databinding.ActivityFetchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFetchBinding
    private lateinit var empList: ArrayList<EmpModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFetchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setHasFixedSize(true)

        empList = arrayListOf()

        // Fetch employees data
        getEmployeesData()
    }

    private fun getEmployeesData() {
        binding.recyclerview.visibility = View.GONE
        binding.loading.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        dbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()) {
                    for (empsnap in snapshot.children) {
                        val empData = empsnap.getValue(EmpModel::class.java)
                        if (empData != null) {
                            empList.add(empData)
                        } else {
                            Log.d("FetchActivity", "empData is null for ${empsnap.key}")
                        }
                    }

                    // Bind data to RecyclerView
                    val mAdapter = EmpAdapter(empList)
                    binding.recyclerview.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()

                    mAdapter.setItemOnClickListner(object :EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchActivity,DetailsActivity::class.java)
                            intent.putExtra("empId",empList[position].id)
                            intent.putExtra("name",empList[position].name)
                            intent.putExtra("age",empList[position].age)
                            intent.putExtra("salary",empList[position].salary)
                            startActivity(intent)
                        }
                    })

                    // Hide loading and show RecyclerView
                    binding.recyclerview.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchActivity", "Database error: ${error.message}")
            }
        })
    }
}
