package net.smartgekko.moviesearch.view.activities

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.databinding.ActivitySettingsBinding
import net.smartgekko.moviesearch.utils.MyApplication
import net.smartgekko.moviesearch.utils.SharedPreference


class SettingsActivity : AppCompatActivity() {
    override fun onBackPressed() = finish()
    private val sharedPreference: SharedPreference = SharedPreference(MyApplication.getAppContext())
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.switchAdult.isChecked =
            sharedPreference.getValueBoolean(getString(R.string.settings_adult_enable))!!
        binding.switchAdult.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            setAdult(b)
        })

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close -> {
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun setAdult(b: Boolean) {
        sharedPreference.saveBoolean(resources.getString(R.string.settings_adult_enable), b)
    }
}