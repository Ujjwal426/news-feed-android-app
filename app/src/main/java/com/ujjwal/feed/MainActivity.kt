package com.ujjwal.feed

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
         fetchDAta()
         mAdapter = NewsListAdapter( this)
        recyclerView.adapter = mAdapter
    }
    private fun fetchDAta(){
       val url = "https://saurav.tech/NewsAPI/top-headlines/category/general/in.json"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val newsJasonArray = it.getJSONArray("articles")
                val newsArray =  ArrayList<News>()
                for(i in 0 until newsJasonArray.length()){
                    val newsJsonObject  = newsJasonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
               mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }
        )
    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}