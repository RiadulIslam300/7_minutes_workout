package com.example.a7minworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items: ArrayList<String>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.
    ViewHolder(binding.root) {
        val llmain = binding.llHistoryRowMain
        val tvRowNumber = binding.tvItemNumber
        val tvDate = binding.tvDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date:String=items[position]
        holder.tvRowNumber.text=(position+1).toString()
        holder.tvDate.text=date

        if (position % 2 == 0) {
            holder.llmain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.lightGray
                )
            )
        }else{
            holder.llmain.setBackgroundColor(ContextCompat.
            getColor(holder.itemView.context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}