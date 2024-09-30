import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import msi.crool.happyplaces.Activity.AddHappyPlaceAct
import msi.crool.happyplaces.Activity.MainActivity
import msi.crool.happyplaces.Databases.DatabaseHandler
import msi.crool.happyplaces.Models.HappyPlacesModel
import msi.crool.happyplaces.R

open class HappyPlacesAdapter(
    private val context: Context,
    private var list: ArrayList<HappyPlacesModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.findViewById<CircleImageView>(R.id.profile_image_home_id).setImageURI(Uri.parse(model.Image))
            holder.itemView.findViewById<TextView>(R.id.name_home_id).text = model.title
            holder.itemView.findViewById<TextView>(R.id.description_home_id).text = model.Description
            holder.itemView.setOnClickListener{
                if(onClickListener!=null)
                {
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }

    fun notifyEditItem(activity:Activity,position: Int,requestCode:Int)
    {
        val intent= Intent(context,AddHappyPlaceAct::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DEATIL,list[position])
        activity.startActivityForResult(intent,requestCode)
        notifyItemChanged(position)
    }

    interface OnClickListener
    {
        fun onClick(position:Int,model: HappyPlacesModel)
    }

    fun removeAt(position: Int)
    {
        val dbHandler=DatabaseHandler(context)
        val isDelete=dbHandler.deleteHappyPlace(list[position])
        if (isDelete>0)
        {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setOnClickListner(onClickListener: OnClickListener)
    {
        this.onClickListener=onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
