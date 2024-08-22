package com.example.ninjacarscalculator.ui.carinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ninjacarscalculator.databinding.ItemCarInfoBinding

class CarInfoAdapter() : RecyclerView.Adapter<MenuViewHolder>() {
    private var data: List<CarInfoModel> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ItemCarInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setData(data: List<CarInfoModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {


        val item = data.getOrNull(position)
        with(holder.binding) {
            count.text = item?.params ?: ""
            names.text = item?.name ?: ""

        }
    }

    override fun getItemCount(): Int = data.size
}


class MenuViewHolder(val binding: ItemCarInfoBinding) : RecyclerView.ViewHolder(binding.root) {
}