package com.example.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.ui.models.Article

class MyAdapter: RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView : View): ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.ivArticle)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val content = itemView.findViewById<TextView>(R.id.tvContent)
        val publeshedDate = itemView.findViewById<TextView>(R.id.tvPublishedAt)
        val source = itemView.findViewById<TextView>(R.id.tvSource)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val asyncListDiffer = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(data.urlToImage).into(holder.image)
            holder.title.text = data.title
            holder.content.text = data.description
            holder.publeshedDate.text = data.publishedAt
            holder.source.text = data.author
            setOnClickListener {
                onClickListener?.let {
                    it(data)
                }
            }
        }
    }
    var onClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onClickListener = listener
    }
}