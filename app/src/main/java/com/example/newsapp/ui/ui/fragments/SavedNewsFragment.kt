package com.example.newsapp.ui.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.adapters.MyAdapter
import com.example.newsapp.ui.ui.MainActivity
import com.example.newsapp.ui.ui.NewsViewModel
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.snackbar.Snackbar
import javax.security.auth.callback.CallbackHandler

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var adapter : MyAdapter
    lateinit var recyclerView : RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setAdapter()
        viewModel.getAllArticles().observe(viewLifecycleOwner){articles ->
            adapter.asyncListDiffer.submitList(articles)

        }

        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.asyncListDiffer.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "article has been deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.upsertArticle(article)
                    }
                    show()
                }
            }


        }

        ItemTouchHelper(swipeToDeleteCallback).apply {
            attachToRecyclerView(recyclerView)
        }

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment2_to_articalFragment2,
                bundle
            )
        }



    }


    fun setAdapter(){
        recyclerView = view?.findViewById(R.id.rvSavedNews)!!
        adapter = MyAdapter()
        recyclerView.adapter =adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}