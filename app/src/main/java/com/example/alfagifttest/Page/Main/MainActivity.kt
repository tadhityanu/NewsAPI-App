package com.example.alfagifttest.Page.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alfagifttest.Adapter.NewsCategory.NewsCategoryAdapter
import com.example.alfagifttest.Data.Model.NewsCategoryModel.NewsCategoryModel
import com.example.alfagifttest.Page.NewsSource.NewsSourceActivity
import com.example.alfagifttest.Page.NewsSource.NewsSourceActivity.Companion.NEWSCATEGORY_ID
import com.example.alfagifttest.R
import com.example.alfagifttest.Util.OnItemNewsCategoryClickCallback
import com.example.alfagifttest.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var newsCategoryAdapter : NewsCategoryAdapter
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setCategoryAdapter()
    }

    fun loadDummyData(context: Context): List<NewsCategoryModel> {
        val inputStream = context.resources.openRawResource(R.raw.news_category)
        val json = inputStream.bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson(json, Array<NewsCategoryModel>::class.java).toList()
    }

    private fun setCategoryAdapter(){
        val categoryList = loadDummyData(this)

        newsCategoryAdapter = NewsCategoryAdapter(this, categoryList)
        val rv = binding.rvCategory
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = newsCategoryAdapter

        newsCategoryAdapter.onItemClickCallback(object : OnItemNewsCategoryClickCallback {
            override fun itemClicked(data: NewsCategoryModel) {
                val intent = Intent(this@MainActivity, NewsSourceActivity::class.java)
                intent.putExtra(NEWSCATEGORY_ID, data.id)
                startActivity(intent)
            }
        })
    }
}