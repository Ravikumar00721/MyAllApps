package com.example.itemtouchhelper

import SwipeGesture
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itemtouchhelper.databinding.ActivityMainBinding
import com.example.itemtouchhelper.ui.theme.ItemTouchHelperTheme
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.Collections

class MainActivity : ComponentActivity() {
    lateinit var binding:ActivityMainBinding

    lateinit var itemRv:RecyclerView
    lateinit var itemlist:ArrayList<String>
    lateinit var itemADpater:ItemRecyclerAdpater
    lateinit var actionRv:RecyclerView
    lateinit var archiveList:ArrayList<String>
    lateinit var archiveAdpater: ItemRecyclerAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemRv=binding.itemListR
        actionRv=binding.acrhiveListR

        itemlist= ArrayList()
        archiveList= ArrayList()

        for (i in 1 .. 20)
        {
            itemlist.add("ITEM-${i}")
        }
        itemADpater= ItemRecyclerAdpater(itemlist,this)
        val itemlayoutmanager=LinearLayoutManager(this)
        itemRv.layoutManager=itemlayoutmanager
        itemRv.adapter=itemADpater

        archiveAdpater=ItemRecyclerAdpater(archiveList,this)
        val archiveLayout=LinearLayoutManager(this)
        actionRv.layoutManager=archiveLayout
        actionRv.adapter=archiveAdpater

        swipeToGesture(itemRv)
    }

    private fun swipeToGesture(itemRv: RecyclerView) {
        val swipeGesture = object : SwipeGesture(this) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return super.onMove(recyclerView, viewHolder, target)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                var actionBtnTapped = false  // Track whether the undo button was clicked

                try {
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            val deleteItem = itemlist[position]
                            itemlist.removeAt(position)
                            itemADpater.notifyItemRemoved(position)

                            // Debugging check
                            Log.d("SNACKBAR", "Displaying Snackbar for deletion")

                            val snackbar = Snackbar.make(binding.root, "Item Deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO") {
                                    itemlist.add(position, deleteItem)
                                    itemADpater.notifyItemInserted(position)
                                    actionBtnTapped = true
                                    Log.d("SNACKBAR", "Undo action clicked for deletion")
                                }.apply {
                                    animationMode = Snackbar.ANIMATION_MODE_FADE
                                }

                            snackbar.setActionTextColor(ContextCompat.getColor(this@MainActivity, R.color.orangered))
                            snackbar.show()

                            // Optionally handle what happens if undo was not clicked after a timeout
                            if (!actionBtnTapped) {
                                // This delay could be adjusted or handled differently based on your needs
                                binding.root.postDelayed({
                                    if (!actionBtnTapped) {
                                        Log.d("SNACKBAR", "Undo action not clicked, performing cleanup")
                                        // Perform any additional cleanup or actions here
                                    }
                                }, Snackbar.LENGTH_LONG.toLong())
                            }
                        }

                        ItemTouchHelper.RIGHT -> {
                            val archiveItem = itemlist[position]
                            itemlist.removeAt(position)
                            archiveList.add(archiveItem)
                            itemADpater.notifyItemRemoved(position)
                            archiveAdpater.notifyItemInserted(archiveList.size - 1)

                            // Debugging check
                            Log.d("SNACKBAR", "Displaying Snackbar for archiving")

                            val snackbar = Snackbar.make(binding.root, "Item Archived", Snackbar.LENGTH_LONG)
                                .setAction("UNDO") {
                                    itemlist.add(position, archiveItem)
                                    itemADpater.notifyItemInserted(position)
                                    archiveList.removeAt(archiveList.size - 1)
                                    archiveAdpater.notifyItemRemoved(archiveList.size - 1)
                                    actionBtnTapped = true
                                    Log.d("SNACKBAR", "Undo action clicked for archiving")
                                }.apply {
                                    animationMode = Snackbar.ANIMATION_MODE_FADE
                                }

                            snackbar.setActionTextColor(ContextCompat.getColor(this@MainActivity, R.color.orangered))
                            snackbar.show()

                            // Optionally handle what happens if undo was not clicked after a timeout
                            if (!actionBtnTapped) {
                                // This delay could be adjusted or handled differently based on your needs
                                binding.root.postDelayed({
                                    if (!actionBtnTapped) {
                                        Log.d("SNACKBAR", "Undo action not clicked, performing cleanup")
                                        // Perform any additional cleanup or actions here
                                    }
                                }, Snackbar.LENGTH_LONG.toLong())
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(itemRv)
    }


}

