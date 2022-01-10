package com.example.newsfresh

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest1


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(this);
         fetchData();
        mAdapter = NewsListAdapter(this);
        findViewById<RecyclerView>(R.id.recyclerView).adapter = mAdapter;


    }
    private fun fetchData(){
        val url = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=2480341fde894b9388e6f9959ac8b7be"
        val request = JsonObjectRequest1(
            Request.Method.GET,
            url,
            null,
            Response.Listener{
                val newsJsonArray = it.getJSONArray("articles")

                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )

                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener{

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    override fun onItemClicked(item: News) {

       val builder =  CustomTabsIntent.Builder();
       val customTabsIntent  = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}