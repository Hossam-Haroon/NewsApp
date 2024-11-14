package com.example.newsapp.ui.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.adapters.MyAdapter
import com.example.newsapp.ui.ui.MainActivity
import com.example.newsapp.ui.ui.NewsViewModel
import com.example.newsapp.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.ui.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var adapter : MyAdapter
    private val TAG = "BreakingNewsError"
    lateinit var progressBar : ProgressBar
    lateinit var rvBreakingNews : RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         progressBar = view.findViewById(R.id.pbProgress)
        viewModel = (activity as MainActivity).viewModel
         rvBreakingNews = view.findViewById(R.id.rvBreakingNews)
        setAdapter()

        adapter.setOnItemClickListener{article->
            //val action = BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticalFragment2(article)
            //findNavController().navigate(action)
            val bundle = Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment2_to_articalFragment2,
                bundle
            )
        }

        viewModel.breakingNews.observe(
            viewLifecycleOwner
        ) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideProgressBar()
                    resource.message?.let {
                        Log.e(TAG, "An error occurred: $it")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {
                        adapter.asyncListDiffer.submitList(it.articles.toList())
                        val totalPages = it.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.pageNumber == totalPages
                        if (isLastPage) {
                            rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }


    fun setAdapter(){


        adapter = MyAdapter()
        rvBreakingNews.adapter = adapter
        rvBreakingNews.layoutManager = LinearLayoutManager(context)
        rvBreakingNews.addOnScrollListener(this@BreakingNewsFragment.scrollListener)


    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem  = firstVisibleItemPosition+ visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition > 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage
                    && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.showBreakingNews("us")
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }
}