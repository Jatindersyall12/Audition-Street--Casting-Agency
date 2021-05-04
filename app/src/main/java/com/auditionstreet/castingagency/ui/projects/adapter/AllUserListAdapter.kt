package com.auditionstreet.castingagency.ui.projects.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.model.response.AllAdminResponse
import com.auditionstreet.castingagency.model.response.AllUsersResponse
import kotlinx.android.synthetic.main.all_admin_item.view.*
import kotlinx.android.synthetic.main.my_project_item.view.*

class AllUserListAdapter(
    val mContext: FragmentActivity, private val mCallback: (
        mposition: String
    ) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AllUsersResponse.Data>() {

        override fun areItemsTheSame(
            oldItem: AllUsersResponse.Data,
            newItem: AllUsersResponse.Data
        ): Boolean {
             return oldItem == newItem

            //return false
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: AllUsersResponse.Data,
            newItem: AllUsersResponse.Data
        ): Boolean {
            return oldItem == newItem
             // return false
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConnectionHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.all_admin_item,
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

    fun submitList(projectResponse: List<AllUsersResponse.Data>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    inner class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: AllUsersResponse.Data) = with(itemView) {
            itemView.chkUser.setOnCheckedChangeListener { buttonView, isChecked ->
                differ.currentList[adapterPosition].isChecked = isChecked
            }
            itemView.chkUser.isChecked = differ.currentList[adapterPosition].isChecked
            itemView.tvAdmin.text=item.name

        }
    }
}
