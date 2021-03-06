package com.example.newsapp.network
/**
 * <h1>Website</h1>
 *<p>
 * Data class for the data obtained by GET from API
 *</p>
 *
 * @author José Ovando 18071
 * @version 1.0
 * @since 2020-06-03
 **/
data class Website (
    var hits: List<News>
)
