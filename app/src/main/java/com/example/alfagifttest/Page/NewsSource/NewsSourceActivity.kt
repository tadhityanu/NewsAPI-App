package com.example.alfagifttest.Page.NewsSource

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
import com.example.alfagifttest.Adapter.NewsSource.NewsSourceAdapter
import com.example.alfagifttest.Data.Model.ApiExceptionModel.ApiException
import com.example.alfagifttest.Data.Model.NewsSourceModel.NewsSourceModel
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem
import com.example.alfagifttest.Page.Article.ListArticleActivity
import com.example.alfagifttest.Page.Article.ListArticleActivity.Companion.NEWSSOURCE_DATA
import com.example.alfagifttest.R
import com.example.alfagifttest.Util.OnItemNewsSourceClickCallback
import com.example.alfagifttest.Util.ViewModelFactory
import com.example.alfagifttest.databinding.ActivityNewsSourceBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsSourceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsSourceBinding
    private lateinit var newsSourceViewModel: NewsSourceViewModel
    private lateinit var newSources: MutableList<SourcesItem>
    private lateinit var newsSourceAdapter: NewsSourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewsSourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val newsCategoryFromIntent = intent?.getStringExtra(NEWSCATEGORY_ID) ?: ""

        setupViewModel()
        setupRecyclerView()
        observeData(newsCategoryFromIntent)
        setSearchList()

    }

    private fun setupViewModel() {
        newsSourceViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[NewsSourceViewModel::class.java]
    }

    private fun setupRecyclerView() {
        newsSourceAdapter = NewsSourceAdapter()
        binding.rvNewsSource.layoutManager = LinearLayoutManager(this)
        binding.rvNewsSource.adapter = newsSourceAdapter
        binding.rvNewsSource.adapter = newsSourceAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { newsSourceAdapter.retry() }
        )

        newsSourceAdapter.addLoadStateListener { loadState ->
            val isListEmpty = newsSourceAdapter.itemCount == 0 && loadState.refresh is LoadState.NotLoading

            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.layoutSource.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.rvNewsSource.isVisible = !isListEmpty
            binding.emptyLayout.root.isVisible = isListEmpty

            if (loadState.source.refresh is LoadState.Error) {
                val errorState = loadState.refresh as LoadState.Error
                println("error state in activity" + errorState.error.message)
                binding.layoutSource.isVisible = false
                if (errorState.error is ApiException) {
                    val apiError = errorState.error as ApiException
                    binding.errorLayout.root.isVisible = true
                    binding.errorLayout.errorMessage.text = apiError.message
                } else {
                    binding.errorLayout.root.isVisible = true
                }
            }
        }

        newsSourceAdapter.onItemClickCallback(object : OnItemNewsSourceClickCallback {
            override fun itemNewsSourceClicked(data: SourcesItem) {
                val intent = Intent(this@NewsSourceActivity, ListArticleActivity::class.java)
                val newsSource = NewsSourceModel(
                    id = data.id ?: "",
                    name = data.name ?: "",
                    description = data.description ?: "",
                    url = data.url ?: "",
                    category = data.category ?: ""
                )
                intent.putExtra(NEWSSOURCE_DATA, newsSource)
                startActivity(intent)
            }
        })
    }

    private fun observeData(category: String) {
        lifecycleScope.launch {
            newsSourceViewModel.getSourceList(category).collectLatest { data ->
                newsSourceAdapter.submitData(data)
            }
        }
    }

    private fun setSearchList() {
        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { newsSourceViewModel.updateSearchQuery(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newsSourceViewModel.updateSearchQuery(newText ?: "")
                return true
            }
        })
    }

    companion object {
        const val NEWSCATEGORY_ID = "newsCategory_id"
    }
}
