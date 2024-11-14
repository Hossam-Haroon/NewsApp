package com.example.newsapp.ui.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.ui.ui.MainActivity
import com.example.newsapp.ui.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticalFragment : Fragment(R.layout.fragment_artical) {

    lateinit var viewModel: NewsViewModel
    val args: ArticalFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val floatingButton = view.findViewById<ExtendedFloatingActionButton>(R.id.fbFloatButtonSaveArticle)
        val webView = view.findViewById<WebView>(R.id.wvWebView)
        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        floatingButton.setOnClickListener {
            viewModel.upsertArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }







    }
}