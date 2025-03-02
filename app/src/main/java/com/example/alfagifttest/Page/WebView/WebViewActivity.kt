package com.example.alfagifttest.Page.WebView

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.alfagifttest.R
import com.example.alfagifttest.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val newsArticleFromIntent = intent?.getStringExtra(WEB_URL)

        setWebView(newsArticleFromIntent)
    }



    override fun onBackPressed() {
        val webView = binding.wvArticle
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setWebView(url: String?) {
        val webView = binding.wvArticle
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        println("article URL" + url)

        if (!url.isNullOrEmpty()) {
            val secureUrl = if (url.startsWith("http://")) {
                url.replace("http://", "https://")
            } else {
                url
            }

            println("secure URL" + secureUrl)

            webView.isVisible = true
            binding.errorLayout.root.isVisible = false
            webView.loadUrl(secureUrl)
        } else{
            webView.isVisible = false
            binding.errorLayout.root.isVisible = true
        }
    }

    companion object {
        const val WEB_URL = "web_url"
    }
}