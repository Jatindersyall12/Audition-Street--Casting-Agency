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
import com.auditionstreet.castingagency.model.response.ProjectResponse
import com.auditionstreet.castingagency.utils.showToast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.home_shortlist_item.view.*
import kotlinx.android.synthetic.main.project_item.view.*
import kotlinx.android.synthetic.main.shortlist_item.view.*
import kotlinx.android.synthetic.main.shortlist_item.view.imgRound
import kotlinx.android.synthetic.main.shortlist_item.view.tvActress
import kotlinx.android.synthetic.main.shortlist_item.view.tvAge
import kotlinx.android.synthetic.main.shortlist_item.view.tvChat
import kotlinx.android.synthetic.main.shortlist_item.view.tvHeight
import kotlinx.android.synthetic.main.shortlist_item.view.tvName
import kotlinx.android.synthetic.main.shortlist_item.view.tvViewProfile
import java.util.*

class ShortListAdapter(
    val mContext: FragmentActivity, private val mCallback: (
        mposition: Int
    ) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProjectResponse.Data>() {

        override fun areItemsTheSame(
            oldItem: ProjectResponse.Data,
            newItem: ProjectResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ProjectResponse.Data,
            newItem: ProjectResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConnectionHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.shortlist_item,
                parent,
                false
            ),
            mContext
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConnectionHolder -> {
                holder.itemView.imgFavourite.setOnClickListener(this)
                holder.itemView.tvChat.setOnClickListener(this)
                holder.itemView.tvViewProfile.setOnClickListener {
                  mCallback.invoke(position)
                }
                holder.itemView.tvName.text = differ.currentList[position].artistName
                if (differ.currentList[position].gender.equals("Male")){
                    holder.itemView.tvActress.text = mContext.getString(R.string.actor)
                }else{
                    holder.itemView.tvActress.text = mContext.getString(R.string.actress)
                }
                holder.itemView.tvAge.text = "Age:"+differ.currentList[position].age
                holder.itemView.tvHeight.text = "Height: "+differ.currentList[position].heightFt+
                        "."+differ.currentList[position].heightIn+"ft"
                Glide.with(mContext).load(differ.currentList[position].image)
                    .into(holder.itemView.imgRound)

//                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(projectResponse: List<ProjectResponse.Data>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProjectResponse.Data) = with(itemView) {
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            itemView.btnViewDetail.background.setTint(color)

            //itemView.tvProject.setTextColor(color)
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.imgFavourite -> {
                showToast(mContext, mContext.resources.getString(R.string.str_coming_soon))
            }
            R.id.tvChat -> {
                showToast(mContext, mContext.resources.getString(R.string.str_coming_soon))

            }
                    }
    }
}
