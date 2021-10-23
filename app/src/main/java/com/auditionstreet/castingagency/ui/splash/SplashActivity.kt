package com.auditionstreet.castingagency.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.auditionstreet.castingagency.databinding.ActivitySplashBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.FirstTimeHereActivity
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.ui.login_signup.AuthorizedUserActivity
import com.auditionstreet.castingagency.utils.AppConstants
import com.silo.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private val binding by viewBinding(ActivitySplashBinding::inflate)

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(3000)
            if (preferences.getString(AppConstants.USER_ID).isEmpty()) {
                val i = Intent(this@SplashActivity, AuthorizedUserActivity::class.java)
                startActivity(i)
            } else {
                if (!preferences.getBoolean(AppConstants.SECOND_TIME_HERE)){
                    val i = Intent(this@SplashActivity, FirstTimeHereActivity::class.java)
                    startActivity(i)
                }else{
                    val i = Intent(this@SplashActivity, FirstTimeHereActivity::class.java)
                    startActivity(i)
                   /* val i = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(i)*/
                }
            }
            finish()
        }
    }
}