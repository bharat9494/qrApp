package com.skqr.skqr.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.skqr.skqr.R
import com.skqr.skqr.data.models.TypeMasterModel

class TypeMasterAdapter(clickListener: OnItemClickListener) : RecyclerView.Adapter<TypeMasterAdapter.ViewHolder>()  {

    private val mListener: OnItemClickListener = clickListener
    var data =  listOf<TypeMasterModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, mListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.textViewNameItemView)
        val selected: ImageView = itemView.findViewById(R.id.imageViewSelected)
        val rootView: ConstraintLayout = itemView.findViewById(R.id.rootView)

        fun bind(item: TypeMasterModel, onItemClickListener: OnItemClickListener) {
            //val res = itemView.context.resources
            name.text = item.tm_name

            if(item.selected) {
                selected.visibility = View.VISIBLE
            } else {
                selected.visibility = View.GONE
            }

            itemView.setOnClickListener { onItemClickListener.onItemClick(item) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_type_master, parent, false)

                return ViewHolder(view)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(typeMasterModel: TypeMasterModel)
    }

}