package com.example.newsapp.news


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.network.*
import kotlinx.coroutines.*
/**
 * <h1>NewsListViewModel</h1>
 *<p>
 * ViewModel for NewsListFragment
 *</p>
 *
 * @author José Ovando 18071
 * @version 1.0
 * @since 2020-06-02
 **/
class NewsListViewModel(private val query: String, private val author: String, private val points: String) : ViewModel() {
    private lateinit var newsDeferred: Deferred<Website>
    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>>
        get() = _newsList


    private val _currentNews = MutableLiveData<News>()
    val currentNews: LiveData<News>
        get() = _currentNews

    private val _status = MutableLiveData<AlgoliaApiStatus>()
    val status: LiveData<AlgoliaApiStatus>
        get() = _status


    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        startStatus()
        getRepos()
    }

    fun startStatus(){
        _status.value = AlgoliaApiStatus.START
    }

    fun openNewsUrl(news: News){
        _currentNews.value = news
    }

    private fun getRepos(){
        coroutineScope.launch {
            if(query != "") {
                newsDeferred = AlgoliaApi.retrofitService.getNewsAsync(query, points = points, author = author)

            } else {
                newsDeferred = AlgoliaApi.retrofitService.getNoQueryNewsAsync(points, author)
            }
            try {
                _status.value = AlgoliaApiStatus.LOADING
                val news = newsDeferred.await().hits
                _status.value = AlgoliaApiStatus.DONE
                _newsList.value = news
            } catch (e: Exception){
                _status.value = AlgoliaApiStatus.ERROR
                _newsList.value = emptyList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
