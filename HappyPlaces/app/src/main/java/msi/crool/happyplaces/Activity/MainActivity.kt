package msi.crool.happyplaces.Activity


import HappyPlacesAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import msi.crool.happyplaces.Databases.DatabaseHandler
import msi.crool.happyplaces.Models.HappyPlacesModel

import msi.crool.happyplaces.databinding.ActivityMainBinding
import msi.crool.happyplaces.utils.SwipeToDeleteCallback
import msi.crool.happyplaces.utils.SwipeToEditCallback

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        binding?.imgBtn?.setOnClickListener {
            val intent= Intent(this, AddHappyPlaceAct::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY)
        }
        getHappyPlaceListFromLDB()

        val deleteSwipeHandler=object :SwipeToDeleteCallback(this)
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter=binding?.recycleId?.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlaceListFromLDB()
            }
        }

        val deleteTouchHelper=ItemTouchHelper(deleteSwipeHandler)
        deleteTouchHelper.attachToRecyclerView(binding?.recycleId)


        val editSwipeHandler=object :SwipeToEditCallback(this)
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter=binding?.recycleId?.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition, ADD_PLACE_ACTIVITY)
            }
        }

        val editTouchHelper=ItemTouchHelper(editSwipeHandler)
        editTouchHelper.attachToRecyclerView(binding?.recycleId)
    }

    private fun getHappyPlaceListFromLDB()
    {
        val dbHandler=DatabaseHandler(this)
        val getHappyPlaceList:ArrayList<HappyPlacesModel> = dbHandler.getHappyPlaceList()

        if(getHappyPlaceList.size>0)
        {
            binding?.textviewID?.visibility= View.GONE
            binding?.recycleId?.visibility=View.VISIBLE
            setUpHappyPlacesREcycleView(getHappyPlaceList)
        }else
        {
            binding?.textviewID?.visibility= View.VISIBLE
            binding?.recycleId?.visibility=View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== ADD_PLACE_ACTIVITY)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                getHappyPlaceListFromLDB()
            }
        }
    }
    private fun setUpHappyPlacesREcycleView(happyPlacesLis:ArrayList<HappyPlacesModel>)
    {
       binding?.recycleId?.layoutManager=LinearLayoutManager(this)
        binding?.recycleId?.setHasFixedSize(true)

        val placesAdapter=HappyPlacesAdapter(this,happyPlacesLis)
        binding?.recycleId?.adapter=placesAdapter

        placesAdapter.setOnClickListner(object :HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlacesModel) {
                val intent=Intent(this@MainActivity,HappyPlaceDetail::class.java)
                intent.putExtra(EXTRA_PLACE_DEATIL,model)
                startActivity(intent)
            }
        })
    }

    companion object{
        var ADD_PLACE_ACTIVITY=1
        var EXTRA_PLACE_DEATIL="EXTRA_PLACE_DETAIL"
    }
}

