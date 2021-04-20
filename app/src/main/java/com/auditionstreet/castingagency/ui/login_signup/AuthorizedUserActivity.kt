package com.auditionstreet.castingagency.ui.login_signup

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.ActivityAuthorizedUserBinding
import com.auditionstreet.castingagency.databinding.ActivityAuthorizedUserBinding.inflate
import com.silo.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizedUserActivity : BaseActivity() {
    private val binding by viewBinding(ActivityAuthorizedUserBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setNavigationController()
    }

    private fun setNavigationController() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
                .navController
        sharedViewModel.navDirectionLiveData.observe(this) {
            navController.navigate(it)
        }
    }
}