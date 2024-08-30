package com.example.letterboxd.ui.activities

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.letterboxd.databinding.ActivityAuthBinding
import com.example.letterboxd.ui.view_models.auth_viewmodels.AuthViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel : AuthViewModel by viewModels()

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {

            setOnExitAnimationListener{ screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.8f,
                    0.4f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 1000L
                zoomX.doOnEnd {
                    screen.remove()
                }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.8f,
                    0.4f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 1000L

                // Remove the splash screen only once after both animations finish
                zoomX.start()
                zoomY.start()
            }

            setKeepOnScreenCondition{
                !viewModel.isReady.value
            }

        }

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val showToast = intent.getBooleanExtra("SHOW_TOAST", false)
        if (showToast) {
            FancyToast.makeText(applicationContext, "Signed out", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show()
        }
    }
}