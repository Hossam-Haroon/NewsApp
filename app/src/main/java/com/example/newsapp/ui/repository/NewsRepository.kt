package com.example.newsapp.ui.repository

import com.example.newsap.ui.api.RetrofitRequest
import com.example.newsap.ui.db.ArticleDatabase
import com.example.newsapp.ui.models.Article

import com.example.newsapp.ui.models.NewsResponse
import retrofit2.Response
import java.util.Locale.IsoCountryCode

class NewsRepository(
    val database: ArticleDatabase
) {

    suspend fun getBreakingNews(pageNumber : Int, countryCode: String) : Response<NewsResponse>{
       return RetrofitRequest.api.getBreakingNews(countryCode, pageNumber)
    }

    suspend fun getSearchNews(searchName :String, pageNumber:Int) : Response<NewsResponse>{
        return RetrofitRequest.api.searchForNews(searchName,pageNumber)
    }

    suspend fun upsertArticle(article : Article) = database.getDao().upsert(article)
    suspend fun deleteArticle(article : Article) = database.getDao().deleteArticle(article)
    fun getAllArticles() = database.getDao().getAllArticles()


}