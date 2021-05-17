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
import com.auditionstreet.castingagency.utils.showImageOrVideoDialog
import com.auditionstreet.castingagency.utils.showToast
import com.bumptech.glide.Glide
import com.silo.model.request.WorkGalleryRequest
import kotlinx.android.synthetic.main.work_item.view.*

class WorkListAdapter(
    val mContext: FragmentActivity, private val mCallback: (
        mposition: Int
    ) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkGalleryRequest>() {

        override fun areItemsTheSame(
            oldItem: WorkGalleryRequest,
            newItem: WorkGalleryRequest
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: WorkGalleryRequest,
            newItem: WorkGalleryRequest
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConnectionHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.work_item,
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
                holder.itemView.imgDelete.setOnClickListener {
                    mCallback.invoke(position)
                }
                /* holder.itemView.chkUser.setOnCheckedChangeListener { buttonView, isChecked ->
                     differ.currentList[position].isChecked = !differ.currentList[position].isChecked
                     // holder.itemView.chkUser.isChecked=!differ.currentList[position].isChecked
                     // notifyDataSetChanged()
                 }*/
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
        //return
    }

    fun submitList(projectResponse: ArrayList<WorkGalleryRequest>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: WorkGalleryRequest) = with(itemView) {

            Glide.with(this).load(item.path)
                .into(itemView.galleryImage)
            if (item.isImage)
                itemView.imgPlay.visibility = View.GONE
            else
                itemView.imgPlay.visibility = View.VISIBLE
            if (item.isShowDeleteImage)
                itemView.imgDelete.visibility = View.VISIBLE
            else
                itemView.imgDelete.visibility = View.GONE

            itemView.imgPlay.setOnClickListener {
                showToast(mContext, "play click")
            }
            itemView.setOnClickListener {
                if (item.isImage)
                    showImageOrVideoDialog(mContext, item.path)
            }
        }
    }
}
