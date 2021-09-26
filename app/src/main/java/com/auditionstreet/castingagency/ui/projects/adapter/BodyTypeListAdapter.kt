package com.auditionstreet.castingagency.ui.projects.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.model.BodyTypeModel
import com.auditionstreet.castingagency.model.LanguageModel
import com.auditionstreet.castingagency.model.response.AllAdminResponse
import kotlinx.android.synthetic.main.all_admin_item.view.*

class BodyTypeListAdapter(
    val mContext: FragmentActivity, private val mCallback: (
        mposition: String
    ) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BodyTypeModel>() {

        override fun areItemsTheSame(
            oldItem: BodyTypeModel,
            newItem: BodyTypeModel
        ): Boolean {
            return oldItem == newItem

            //return false
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: BodyTypeModel,
            newItem: BodyTypeModel
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
                // holder.itemView.chkUser.isChecked=differ.currentList[position].isChecked


            }
        }
    }

    /* override fun getItemViewType(position: Int): Int {
         return 1
     }*/

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(projectResponse: ArrayList<BodyTypeModel>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    inner class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BodyTypeModel) = with(itemView) {
            itemView.chkUser.setOnCheckedChangeListener { buttonView, isChecked ->
                differ.currentList[adapterPosition].isChecked = isChecked
            }
            itemView.chkUser.isChecked = differ.currentList[adapterPosition].isChecked
            itemView.tvAdmin.text = item.bodyType
        }
    }
}
