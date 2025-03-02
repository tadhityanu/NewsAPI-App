package com.example.alfagifttest.Page.Article

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfagifttest.Adapter.LoadingStateAdapter.LoadingStateAdapter
import com.example.alfagifttest.Adapter.NewsArticleAdapter.NewsArticleAdapter
import com.example.alfagifttest.Data.Model.ApiExceptionModel.ApiException
import com.example.alfagifttest.Data.Model.NewsSourceModel.NewsSourceModel
import com.example.alfagifttest.Data.Response.NewsArticle.ArticlesItem
import com.example.alfagifttest.Page.WebView.WebViewActivity
import com.example.alfagifttest.Page.WebView.WebViewActivity.Companion.WEB_URL
import com.example.alfagifttest.R
import com.example.alfagifttest.Util.OnItemNewsArticleClickCallback
import com.example.alfagifttest.Util.ViewModelFactory
import com.example.alfagifttest.databinding.ActivityListArticleBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListArticleBinding
    private lateinit var newsArticleViewModel: ListArticleViewModel
    private lateinit var newsArticleAdapter: NewsArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val newsSourceFromIntent = intent?.getParcelableExtra<NewsSourceModel>(NEWSSOURCE_DATA)

        setupViewModel()
        setSourceData(newsSourceFromIntent)
        setupRecyclerView()
        observeData(newsSourceFromIntent)
        openSourceURL(newsSourceFromIntent)
        setSearchList()
    }

    private fun setupViewModel() {
        newsArticleViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[ListArticleViewModel::class.java]
    }

    private fun setSourceData(data: NewsSourceModel?) {
        binding.tvSourceName.text = data?.name
        binding.tvSourceDescription.text = data?.description
        binding.tvSourceCategory.text = data?.category
    }

    private fun setupRecyclerView() {
        newsArticleAdapter = NewsArticleAdapter()
        binding.rvArticle.layoutManager = LinearLayoutManager(this)
        binding.rvArticle.adapter = newsArticleAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { newsArticleAdapter.retry() }
        )

        newsArticleAdapter.addLoadStateListener { loadState ->
            val isListEmpty = newsArticleAdapter.itemCount == 0 && loadState.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.rvArticle.isVisible = !isListEmpty
            binding.emptyLayout.root.isVisible = isListEmpty

            if (loadState.source.refresh is LoadState.Error) {
                val errorState = loadState.refresh as LoadState.Error
                println("error state in activity" + errorState.error.message)
                binding.layoutArticle.isVisible = false
                if (errorState.error is ApiException) {
                    val apiError = errorState.error as ApiException
                    binding.errorLayout.root.isVisible = true
                    binding.errorLayout.errorMessage.text = apiError.message
                } else {
                    binding.errorLayout.root.isVisible = true
                }
            }
        }

        newsArticleAdapter.onItemClickCallback(object : OnItemNewsArticleClickCallback {
            override fun itemNewsArticleClicked(data: ArticlesItem) {
                val intent = Intent(this@ListArticleActivity, WebViewActivity::class.java)
                intent.putExtra(WEB_URL, data.url)
                startActivity(intent)
            }
        })
    }

    private fun observeData(data: NewsSourceModel?) {
        print("data source di activity " + data?.category)
        newsArticleViewModel.setDefaultQuery("")
        lifecycleScope.launch {
            newsArticleViewModel.articlesFlow(data?.id ?: "").collectLatest { data ->
                newsArticleAdapter.submitData(data)
            }
        }
    }

    private fun openSourceURL(data: NewsSourceModel?) {
        binding.imgWebView.setOnClickListener {
            val intent = Intent(this@ListArticleActivity, WebViewActivity::class.java)
            intent.putExtra(WEB_URL, data?.url)
            startActivity(intent)
        }
    }

    private fun setSearchList() {
        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { newsArticleViewModel.searchArticles(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newsArticleViewModel.searchArticles(newText ?: "")
                return true
            }
        })
    }

    companion object {
        const val NEWSSOURCE_DATA = "source_data"
    }
}