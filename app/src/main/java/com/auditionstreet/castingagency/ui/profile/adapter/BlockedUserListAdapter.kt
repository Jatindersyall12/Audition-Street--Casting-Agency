package com.auditionstreet.castingagency.ui.profile.adapter

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
import com.auditionstreet.castingagency.model.response.BlockUserListResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.block_user_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class BlockedUserListAdapter(
    val mContext: FragmentActivity, private val mCallback: (mposition: String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlockUserListResponse.Data>() {

        override fun areItemsTheSame(
            oldItem: BlockUserListResponse.Data,
            newItem: BlockUserListResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: BlockUserListResponse.Data,
            newItem: BlockUserListResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConnectionHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.block_user_list_item,
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
                holder.itemView.btnUnblock.setOnClickListener {
                    mCallback.invoke(position.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(projectResponse: ArrayList<BlockUserListResponse.Data>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BlockUserListResponse.Data) = with(itemView) {
           itemView.tvName.text = item.artistName
            if (item.gender.equals("Male")){
                itemView.tvActress.text = mContext.getString(R.string.actor)
            }else{
                itemView.tvActress.text = mContext.getString(R.string.actress)
            }
            itemView.tvAge.text = "Age:"+item.age
            itemView.tvHeight.text = "Height: "+item.heightFt+
                    "."+item.heightIn+"ft"
            if(item.artistImage.isNotEmpty()) {
                Glide.with(mContext).load(item.artistImage)
                    .into(itemView.imgRound)
            }
        }
    }
}
