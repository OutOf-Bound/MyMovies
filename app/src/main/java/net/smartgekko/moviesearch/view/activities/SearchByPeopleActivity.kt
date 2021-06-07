package net.smartgekko.moviesearch.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import net.smartgekko.moviesearch.R

class SearchByPeopleActivity : AppCompatActivity() {

    override fun onBackPressed() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_people)

        fun goSearch() {
            val intent = Intent(applicationContext, SearchActivity::class.java)
            startActivity(intent)
        }

        fun goHome() {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        fun goFavorites() {
            val intent = Intent(applicationContext, FavoritesActivity::class.java)
            startActivity(intent)
        }

        val topAppBar = findViewById<View>(R.id.topAppBar) as MaterialToolbar

        topAppBar.setNavigationOnClickListener {
            goSearch()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    goHome()
                    true
                }
                R.id.favorite -> {
                    goFavorites()
                    true
                }
                else -> false
            }
        }
    }
}