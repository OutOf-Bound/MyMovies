package net.smartgekko.moviesearch.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import net.smartgekko.moviesearch.R
import net.smartgekko.moviesearch.databinding.ActivityMainBinding
import net.smartgekko.moviesearch.utils.ANIM_TIMER_PERIOD

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        val view = binding.getRoot()
        setContentView(view)

        fun goHome() {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        fun doAnimationSet3(v: Array<View>, a: Array<Animation>) {
            val scale1Listener: Animation.AnimationListener = object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    try {
                        Thread.sleep(ANIM_TIMER_PERIOD)
                    } catch (e: Exception) {
                    }
                    v[3].startAnimation(a[3])
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // TODO Auto-generated method stub
                }

                override fun onAnimationStart(animation: Animation) {
                    // TODO Auto-generated method stub
                }
            }
            val alpha1Listener: Animation.AnimationListener = object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    v[3].visibility = View.INVISIBLE
                    goHome()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // TODO Auto-generated method stub
                }

                override fun onAnimationStart(animation: Animation) {
                    // TODO Auto-generated method stub
                }
            }
            a[2].setAnimationListener(scale1Listener)
            a[3].setAnimationListener(alpha1Listener)
            v[0].startAnimation(a[0])
            v[1].startAnimation(a[1])
            v[2].startAnimation(a[2])
        }

        fun showStartLogo() {
            val anim1 = AnimationUtils.loadAnimation(this, R.anim.myscale)
            val anim2 = AnimationUtils.loadAnimation(this, R.anim.myscale)
            val anim3 = AnimationUtils.loadAnimation(this, R.anim.myscale)
            val anim4 = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
            doAnimationSet3(
                arrayOf(
                    binding.textLogo,
                    binding.textLogoBy,
                    binding.imageLogo,
                    binding.logoLayout
                ), arrayOf(anim1, anim2, anim3, anim4)
            )
        }
        showStartLogo()
    }
}