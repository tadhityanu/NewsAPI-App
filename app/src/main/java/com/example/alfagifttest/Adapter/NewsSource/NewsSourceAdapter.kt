package com.example.alfagifttest.Adapter.NewsSource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem
import com.example.alfagifttest.Util.OnItemNewsCategoryClickCallback
import com.example.alfagifttest.Util.OnItemNewsSourceClickCallback
import com.example.alfagifttest.databinding.ItemNewsSourceBinding

class NewsSourceAdapter :
    PagingDataAdapter<SourcesItem, NewsSourceAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemNewsSourceClickCallback: OnItemNewsSourceClickCallback

    fun onItemClickCallback(itemClick: OnItemNewsSourceClickCallback) {
        this.onItemNewsSourceClickCallback = itemClick
    }

    inner class ViewHolder(val binding: ItemNewsSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SourcesItem) {
            binding.apply {
                tvSourceName.text = data.name
                tvSourceCategory.text = data.category
                tvSourceDescription.text = data.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNewsSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemNewsSourceClickCallback.itemNewsSourceClicked(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SourcesItem>() {
            override fun areItemsTheSame(oldItem: SourcesItem, newItem: SourcesItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SourcesItem, newItem: SourcesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}