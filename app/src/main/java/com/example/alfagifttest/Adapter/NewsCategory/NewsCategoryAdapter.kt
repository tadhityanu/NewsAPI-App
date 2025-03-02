package com.example.alfagifttest.Adapter.NewsCategory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfagifttest.Data.Model.NewsCategoryModel.NewsCategoryModel
import com.example.alfagifttest.Util.OnItemNewsCategoryClickCallback
import com.example.alfagifttest.databinding.ItemNewsCategoryBinding

class NewsCategoryAdapter(val context : Context, val listCategory: List<NewsCategoryModel>) :
    RecyclerView.Adapter<NewsCategoryAdapter.ViewHolder>() {

    private lateinit var onItemNewsCategoryClickCallback: OnItemNewsCategoryClickCallback

    fun onItemClickCallback(itemClick : OnItemNewsCategoryClickCallback){
        this.onItemNewsCategoryClickCallback = itemClick
    }

    inner class ViewHolder(val binding: ItemNewsCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NewsCategoryModel) {
            val imageId = context.resources.getIdentifier(data.image, "drawable", context.packageName)

            binding.apply {
                tvNewsCategory.text = data.category
                Glide.with(itemView)
                    .load(imageId)
                    .into(binding.imgBackground)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemNewsCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCategory[position])
        holder.itemView.setOnClickListener {
            onItemNewsCategoryClickCallback.itemClicked(listCategory[position])
        }
    }
}