package com.example.newsapp.ui.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.ui.models.Article
import com.example.newsapp.ui.models.NewsResponse
import com.example.newsapp.ui.repository.NewsRepository
import com.example.newsapp.ui.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val repository: NewsRepository
): ViewModel() {

    private val _breakingNews  = MutableLiveData<Resource<NewsResponse>>()
    val breakingNews: LiveData<Resource<NewsResponse>> get() = _breakingNews
    var pageNumber = 1
    var breakingNewsResponse : NewsResponse? = null
    private val _searchNews  = MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>> get() = _searchNews
    var pageNumberSearchNews = 1
    var searchNewsResponse : NewsResponse? = null
    init {
        showBreakingNews("us")
    }
//---------------------------------------------------------------------
fun showBreakingNews(countryCode : String){
        viewModelScope.launch {
            _breakingNews.postValue(Resource.Loading())
           val response= repository.getBreakingNews(pageNumber, countryCode)
            _breakingNews.postValue(handleBreakingNewsResponse(response))
        }
    }

    private fun handleBreakingNewsResponse(response : Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { newsResponse ->
                pageNumber++
                if (breakingNewsResponse == null){
                    breakingNewsResponse = newsResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    oldArticles?.addAll(newsResponse.articles)

                }
                return Resource.Success(breakingNewsResponse?: newsResponse)
            }
        }
        return Resource.Error(response.message())
    }
//--------------------------------------------------------------
     fun getSearchNews(searchName : String){
        viewModelScope.launch {
            _searchNews.postValue(Resource.Loading())
            val response = repository.getSearchNews(searchName,pageNumber)
            _searchNews.postValue(handleSearchNews(response))
        }
    }
    private fun handleSearchNews(response : Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { newsResponse ->
                pageNumberSearchNews++
                if (searchNewsResponse == null){
                    searchNewsResponse = newsResponse
                }else{
                    val oldArticles = searchNewsResponse?.articles
                    oldArticles?.addAll(newsResponse.articles)
                }
                return Resource.Success(searchNewsResponse?: newsResponse)
            }
        }
        return Resource.Error(response.message())
    }
//----------------------------------------------------------------
    fun upsertArticle(article : Article) = viewModelScope.launch {
        repository.upsertArticle(article)
    }
    fun deleteArticle(article : Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }
    fun getAllArticles() = repository.getAllArticles()


}