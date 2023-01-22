package rachman.forniandi.gatherplacesapp.adapters

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.DatabaseErrorHandler
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rachman.forniandi.gatherplacesapp.AddPlaceActivity
import rachman.forniandi.gatherplacesapp.MainActivity
import rachman.forniandi.gatherplacesapp.databinding.ItemPlaceBinding
import rachman.forniandi.gatherplacesapp.dbHandler.DatabaseHandler
import rachman.forniandi.gatherplacesapp.models.DataPlaceModel

class GatherPlaceAdapter(private val context: Context,
                         private var listPlace:ArrayList<DataPlaceModel>):RecyclerView.Adapter<GatherPlaceAdapter.GatherPlaceHolder>() {

    private var onClickListener: OnClickListener? = null

    class GatherPlaceHolder (binding: ItemPlaceBinding): RecyclerView.ViewHolder(binding.root){
        val imgPlace = binding.imgRoundPlace
        val txtTitle = binding.txtTitle
        val txtDescription = binding.txtDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GatherPlaceHolder {
        return GatherPlaceHolder(ItemPlaceBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false))
    }

    override fun onBindViewHolder(holder: GatherPlaceHolder, position: Int) {
        val model = listPlace[position]
        holder.txtTitle.text = model.title
        holder.txtDescription.text = model.description
        holder.imgPlace.setImageURI(Uri.parse(model.image))

        holder.itemView.setOnClickListener {

            if (onClickListener != null) {
                onClickListener?.onClick(position, model)
            }
        }

    }

    override fun getItemCount(): Int {
        return listPlace.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: DataPlaceModel)
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,listPlace[position])
        activity.startActivityForResult(intent,requestCode)

        notifyItemChanged(position)
    }

    fun removeItemAt(position: Int){
        val dbHandler = DatabaseHandler(context)
        var isDeleted = dbHandler.deleteDataPlacesList(listPlace[position])

        if (isDeleted > 0){
            listPlace.removeAt(position)
            notifyItemRemoved(position)
        }
    }


}