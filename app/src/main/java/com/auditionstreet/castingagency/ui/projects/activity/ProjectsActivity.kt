package com.auditionstreet.castingagency.ui.projects.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.ActivityProjectsBinding
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.chat.DialogsActivity
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.utils.DataHelper
import com.auditionstreet.castingagency.utils.showToast
import com.silo.ui.base.BaseActivity
import com.silo.utils.changeIcons
import com.silo.utils.network.IconPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@AndroidEntryPoint
class ProjectsActivity : BaseActivity() {
    private val binding by viewBinding(ActivityProjectsBinding::inflate)
    private lateinit var imageIcons: ArrayList<ImageView>
    private lateinit var bottomBarText: ArrayList<TextView>
    private lateinit var activeIcons: ArrayList<Int>
    private lateinit var inActiveIcons: ArrayList<Int>

    @Inject
    lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpToolbar(toolbar,getString(R.string.str_project),false,true)
        setNavigationController()
        imageIcons = arrayListOf(
            binding.footerHome.homeButton,
            binding.footerHome.projectButton,
            binding.footerHome.chatButton,
            binding.footerHome.accountButton,
        )
        bottomBarText = arrayListOf(
            binding.footerHome.tvHome,
            binding.footerHome.tvProjects,
            binding.footerHome.tvChat,
            binding.footerHome.tvAccount,
        )
        activeIcons = DataHelper.activeIcons
        inActiveIcons = DataHelper.inActiveIcons
        onTabClicks()
    }

    private fun onTabClicks() {
        binding.footerHome.llHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            changeIcons(
                imageIcons,
                activeIcons,
                inActiveIcons,
                IconPosition.PROJECTS.value,
                bottomBarText,
                this
            )
        }
        binding.footerHome.llProjects.setOnClickListener {
            changeIcons(
                imageIcons,
                activeIcons,
                inActiveIcons,
                IconPosition.PROJECTS.value,
                bottomBarText,
                this
            )
        }
        binding.footerHome.llChat.setOnClickListener {

            val intent = Intent(this, DialogsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            changeIcons(
                imageIcons,
                activeIcons,
                inActiveIcons,
                IconPosition.CHAT.value,
                bottomBarText,
                this
            )
        }
        binding.footerHome.llAccount.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            changeIcons(
                imageIcons,
                activeIcons,
                inActiveIcons,
                IconPosition.ACCOUNT.value,
                bottomBarText,
                this
            )
        }
    }

    override fun onResume() {
        super.onResume()
        changeIcons(
            imageIcons,
            activeIcons,
            inActiveIcons,
            IconPosition.PROJECTS.value,
            bottomBarText,
            this
        )

    }
    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navProject) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        if (navController.graph.startDestination == navController.currentDestination?.id) {
            val i = Intent(this, HomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            finish()
        } else
            super.onBackPressed()
    }

    private fun setNavigationController() {
        val navController = (supportFragmentManager.findFragmentById(R.id.navProject) as NavHostFragment)
            .navController
        sharedViewModel.navDirectionLiveData.observe(this){
            navController.navigate(it)
        }
    }
}