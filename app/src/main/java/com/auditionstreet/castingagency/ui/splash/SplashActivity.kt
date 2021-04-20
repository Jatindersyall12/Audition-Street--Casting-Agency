package com.auditionstreet.castingagency.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.auditionstreet.castingagency.databinding.ActivitySplashBinding
import com.auditionstreet.castingagency.ui.login_signup.AuthorizedUserActivity
import com.silo.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private val binding by viewBinding(ActivitySplashBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, AuthorizedUserActivity::class.java))
            finish()
        }
    }
}