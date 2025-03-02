package com.example.alfagifttest.Adapter.NewsArticleAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alfagifttest.Data.Response.NewsArticle.ArticlesItem
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem
import com.example.alfagifttest.Util.DateUtil
import com.example.alfagifttest.Util.OnItemNewsArticleClickCallback
import com.example.alfagifttest.Util.OnItemNewsSourceClickCallback
import com.example.alfagifttest.databinding.ItemArticleBinding
import com.example.alfagifttest.databinding.ItemNewsSourceBinding
import java.util.Date

class NewsArticleAdapter :
    PagingDataAdapter<ArticlesItem, NewsArticleAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemNewsArticleClickCallback: OnItemNewsArticleClickCallback

    fun onItemClickCallback(itemClick: OnItemNewsArticleClickCallback) {
        this.onItemNewsArticleClickCallback = itemClick
    }

    inner class ViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArticlesItem) {
            binding.apply {
                tvArticleTitle.text = data.title
                tvArticleAuthor.text = data.author
                tvSourceDescription.text = data.description
                tvArticleDate.text = DateUtil.formatDate(data.publishedAt )
                Glide.with(itemView)
                    .load(data.urlToImage)
                    .into(imgArticle)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemNewsArticleClickCallback.itemNewsArticleClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}