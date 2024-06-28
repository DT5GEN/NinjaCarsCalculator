package com.example.ninjacarscalculator.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ninjacarscalculator.models.Task2
import com.example.ninjacarscalculator.databinding.ItemAdapterBinding

class AdapterAutoName(private val onClick: (Task2) -> Unit,
): RecyclerView.Adapter<MenuViewHolder>() {
    private var data: List<Task2> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    fun setData(data: List<Task2>) {
        this.data = data
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {




        val item = data.getOrNull(position)
        with(holder.binding) {
          numb.text = "${position+1}"
            name.text = item?.nameAuto?:""
            layout.setOnClickListener {
                onClick(item!!)
            }
        }
    }

    override fun getItemCount(): Int = data.size
}


class MenuViewHolder(val binding: ItemAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
}