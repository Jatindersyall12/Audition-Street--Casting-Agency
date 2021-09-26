package com.auditionstreet.castingagency.ui.home.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.ActivityFirstTimeHereBinding
import com.auditionstreet.castingagency.databinding.ActivityHomeBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.projects.activity.ProfileActivity
import com.auditionstreet.castingagency.ui.projects.activity.ProjectsActivity
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.DataHelper
import com.auditionstreet.castingagency.utils.showExitDialog
import com.auditionstreet.castingagency.utils.showToast
import com.bumptech.glide.Glide
import com.silo.ui.base.BaseActivity
import com.silo.utils.changeIcons
import com.silo.utils.network.IconPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@AndroidEntryPoint
class FirstTimeHereActivity : BaseActivity() {
    private val binding by viewBinding(ActivityFirstTimeHereBinding::inflate)

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setNavigationController()
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navhostfirstTimeHere) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        if (navController.graph.startDestination == navController.currentDestination?.id){
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
        finish()
        }
        // closeAppDialog(this)
        else
            super.onBackPressed()
    }

    private fun setNavigationController() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.navhostfirstTimeHere) as NavHostFragment)
                .navController
        sharedViewModel.navDirectionLiveData.observe(this) {
            navController.navigate(it)
        }
    }
}