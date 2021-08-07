package com.auditionstreet.castingagency.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.model.response.HomeApiResponse
import com.auditionstreet.castingagency.model.response.ProjectResponse
import kotlinx.android.synthetic.main.home_shortlist_item.view.*
import java.util.*

class HomeShortListAdapter(
    val mContext: FragmentActivity, private val mCallback: (
        mposition: Int
    ) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeApiResponse.Data.Accept>() {

        override fun areItemsTheSame(
            oldItem: HomeApiResponse.Data.Accept,
            newItem: HomeApiResponse.Data.Accept
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: HomeApiResponse.Data.Accept,
            newItem: HomeApiResponse.Data.Accept
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConnectionHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.home_shortlist_item,
                parent,
                false
            ),
            mContext
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConnectionHolder -> {
//                holder.bind(differ.currentList[position])
                holder.itemView.tvViewProfile.setOnClickListener {
                    mCallback.invoke(position)
                }
                holder.itemView.tvName.text = differ.currentList[position].artistName
                holder.itemView.tvActress.text = "Coming Soon"
                holder.itemView.tvAge.text = "Age:"+differ.currentList[position].age
                holder.itemView.tvHeight.text = "Height: "+differ.currentList[position].heightFt+
                        "."+differ.currentList[position].heightIn+"ft"
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(projectResponse: List<HomeApiResponse.Data.Accept>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HomeApiResponse.Data.Accept) = with(itemView) {
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
           // itemView.btnViewDetail.background.setTint(color)
            //itemView.tvProject.setTextColor(color)
        }
    }
}
