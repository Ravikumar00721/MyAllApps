package msi.crool.recycleview

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import msi.crool.recycleview.databinding.ActivityMainBinding
import android.widget.Toast

class MainActivity : ComponentActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var editIcon: Drawable
    private lateinit var deleteIcon: Drawable
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Initialize icons
        editIcon = ContextCompat.getDrawable(this, R.drawable.r_edit)!!
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.r_delete)!!

        // Initialize the adapter
        adapter = MainAdapter(TaskList.tasklist.toMutableList())
        binding?.recycleViewId?.adapter = adapter
        binding?.recycleViewId?.layoutManager = LinearLayoutManager(this)

        // Attach ItemTouchHelper for swipe actions
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                // Not needed for swipe functionality
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    // Handle delete action
                    val deletedTask = adapter.deleteItem(position)
                    Toast.makeText(this@MainActivity, "Task Deleted", Toast.LENGTH_LONG).show()
                    // Handle Undo action if needed
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Handle edit action (for simplicity, editing title here)
                    adapter.editItem(position, "Edited Task", "Edited Time")
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView

                if (dX > 0) {
                    // Draw edit icon for right swipe
                    drawEditIcon(c, itemView, dX)
                } else if (dX < 0) {
                    // Draw delete icon for left swipe
                    drawDeleteIcon(c, itemView, dX)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            private fun drawEditIcon(c: Canvas, itemView: View, dX: Float) {
                val iconTop = itemView.top + (itemView.height - editIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + editIcon.intrinsicHeight
                val iconLeft = itemView.left + 16.dpToPx() // Adjust margin as needed
                val iconRight = iconLeft + editIcon.intrinsicWidth
                editIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                editIcon.draw(c)
            }

            private fun drawDeleteIcon(c: Canvas, itemView: View, dX: Float) {
                val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + deleteIcon.intrinsicHeight
                val iconRight = itemView.right - 16.dpToPx() // Adjust margin as needed
                val iconLeft = iconRight - deleteIcon.intrinsicWidth
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(c)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding?.recycleViewId)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}
