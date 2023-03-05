package com.cs446group18.delaywise

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SearchableActivity : AppCompatActivity() {

    override fun onSearchRequested(): Boolean {
        println("onSearchRequested (SearchableActivity) called!")
        return true;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                println("search query: $query")
            }
        }
    }
}
