package com.auditionstreet.castingagency.ui.home.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.ActivityHomeBinding
import com.auditionstreet.castingagency.utils.DataHelper
import com.silo.ui.base.BaseActivity
import com.silo.utils.changeIcons
import com.silo.utils.network.IconPosition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private val binding by viewBinding(ActivityHomeBinding::inflate)
    private lateinit var imageIcons: ArrayList<ImageView>
    private lateinit var bottomBarText: ArrayList<TextView>

    private lateinit var activeIcons: ArrayList<Int>
    private lateinit var inActiveIcons: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
            changeIcons(
                imageIcons,
                activeIcons,
                inActiveIcons,
                IconPosition.HOME.value,
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
            /*val intent = Intent(this, EventActivity::class.java)
            startActivity(intent)*/
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
            IconPosition.HOME.value,
            bottomBarText,
            this
        )

    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostHomeFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        if (navController.graph.startDestination == navController.currentDestination?.id)
        // closeAppDialog(this)
        else
            super.onBackPressed()
    }
}