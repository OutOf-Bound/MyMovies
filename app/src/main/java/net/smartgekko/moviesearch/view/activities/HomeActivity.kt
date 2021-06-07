package net.smartgekko.moviesearch.view.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.databinding.ActivityHomeBinding
import net.smartgekko.moviesearch.utils.*
import net.smartgekko.moviesearch.view.fragments.PopularMoviesFragment
import net.smartgekko.moviesearch.view.fragments.TopMoviesFragment
import net.smartgekko.moviesearch.view.fragments.UpcomingMoviesFragment


class HomeActivity : FragmentActivity() {
    override fun onBackPressed() {}

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val receiver = AppBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceClass = CheckInetConnectionService::class.java
        val intent = Intent(this, serviceClass)
        if (!isServiceRunning(serviceClass)) {
            startService(intent)
        }

        val titles = arrayOf(
            getString(R.string.popular),
            getString(R.string.top),
            getString(R.string.upcoming)
        )

        viewPager = binding.pager
        tabLayout = binding.tabLayout

        val pagerAdapter = FragmentSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.setText(
                titles.get(position)
            )
        }.attach()

        val myapp = MyApplication(this)
        myapp.create()

        val topAppBar = binding.topAppBar
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    goFavorites()
                    true
                }
                R.id.settings -> {
                    goSettings()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        filter.addAction(INTENT_ACTION_KEY)
        registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private inner class FragmentSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES_HOME
        override fun createFragment(position: Int): Fragment {

            return when (position) {
                0 -> {
                    PopularMoviesFragment.newInstance()
                }
                1 -> {
                    TopMoviesFragment.newInstance()
                }
                2 -> {
                    UpcomingMoviesFragment.newInstance()
                }

                else -> PopularMoviesFragment.newInstance()
            }
        }
    }

    fun goSearch() {
        val intent = Intent(applicationContext, SearchActivity::class.java)
        startActivity(intent)
    }

    fun goFavorites() {
        val intent = Intent(applicationContext, FavoritesActivity::class.java)
        startActivity(intent)
    }

    fun goSettings() {
        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
    }
}