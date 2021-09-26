package com.auditionstreet.castingagency.ui.firstTimeHere.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.model.FirstTimeHereModel
import kotlinx.android.synthetic.main.first_time_item.view.*

class FirstTimeHereAdapter(
    private val mContext: FragmentActivity
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FirstTimeHereModel>() {

        override fun areItemsTheSame(
            oldItem: FirstTimeHereModel,
            newItem: FirstTimeHereModel
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: FirstTimeHereModel,
            newItem: FirstTimeHereModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ConnectionHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.first_time_item,
                parent,
                false
            ),
            mContext
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConnectionHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(sliderResponse: ArrayList<FirstTimeHereModel>) {
        differ.submitList(sliderResponse)
    }

    class ConnectionHolder(
        itemView: View,
        private val mContext: FragmentActivity
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: FirstTimeHereModel) = with(itemView) {
            itemView.ivData.setImageResource(item.firstTimeImage)
            itemView.tvBriefOfScreen.text = item.firstTimeText
        }
    }
}
