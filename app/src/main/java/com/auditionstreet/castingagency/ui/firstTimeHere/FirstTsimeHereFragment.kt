package com.auditionstreet.castingagency.ui.firstTimeHere

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.databinding.FragmentFirstTimeHereBinding
import com.auditionstreet.castingagency.model.FirstTimeHereModel
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.firstTimeHere.adapter.FirstTimeHereAdapter
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.utils.AppConstants
import com.auditionstreet.castingagency.utils.DataHelper
import com.google.android.material.tabs.TabLayoutMediator
import com.silo.utils.AppBaseFragment
import com.silo.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirstTsimeHereFragment : AppBaseFragment(R.layout.fragment_first_time_here), View.OnClickListener {
    private val binding by viewBinding(FragmentFirstTimeHereBinding::bind)
    private lateinit var pagerAdapter: FirstTimeHereAdapter
    private var mstate: Int=0

    @Inject
    lateinit var preferences: Preferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences.setBoolean(AppConstants.SECOND_TIME_HERE, true)
        init()
        setListner()
    }

    private fun setListner() {

        binding.firstTimePager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mstate=0
                if (position == binding.firstTimePager.adapter!!.itemCount - 1)
                    binding.tvSwipeContinue.text = resources.getString(R.string.swipe_to_complete)
                else
                    binding.tvSwipeContinue.text = resources.getString(R.string.swipe_to_continue)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if(mstate==2&&position==5) {
                    val i = Intent(requireActivity(), HomeActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                ++mstate;
            }
        })
    }

    fun init() {
        pagerAdapter = FirstTimeHereAdapter(requireActivity())
        binding.firstTimePager.adapter = pagerAdapter
        val firstTimeHereList = ArrayList<FirstTimeHereModel>()
        for (i in 0 until DataHelper.firstTimehere.size){
            val firstTimeHereModel = FirstTimeHereModel()
            firstTimeHereModel.firstTimeImage =  DataHelper.firstTimehere[i]
            firstTimeHereModel.firstTimeText = DataHelper.firstTimehereString[i]
            firstTimeHereList.add(firstTimeHereModel)
        }
        pagerAdapter.submitList(firstTimeHereList)
        TabLayoutMediator(binding.tabWalkThrough, binding.firstTimePager)
        { tab, position -> }.attach()
    }

    override fun onClick(v: View?) {

    }


}