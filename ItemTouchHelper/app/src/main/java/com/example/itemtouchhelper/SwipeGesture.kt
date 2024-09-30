import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.itemtouchhelper.ItemRecyclerAdpater
import com.example.itemtouchhelper.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.Collections

abstract class SwipeGesture(context: Context) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, // Enable dragging up and down
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Enable swiping left and right
) {
    private val deleteColor = ContextCompat.getColor(context, R.color.red)
    private val deleteIcon = R.drawable.r_delete // Ensure this icon exists

    private val archiveColor = ContextCompat.getColor(context, R.color.blue)
    private val archiveIcon = R.drawable.r_archive // Ensure this icon exists

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeRightBackgroundColor(archiveColor)
            .addSwipeRightActionIcon(archiveIcon)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.adapterPosition != target.adapterPosition) {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            // Swap items in the data list
            (recyclerView.adapter as ItemRecyclerAdpater).onItemMove(fromPosition, toPosition)
        }
        return true
    }
}
