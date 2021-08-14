package com.auditionstreet.castingagency.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.model.response.ApplicationResponse
import com.auditionstreet.castingagency.model.response.MyProjectResponse
import com.auditionstreet.castingagency.utils.playVideo
import com.auditionstreet.castingagency.utils.showToast
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.all_application_item.view.*

@Suppress("DEPRECATION")
class AllApplicationsAdapter(
    val mContext: FragmentActivity, private val mCallback: (
        mposition: Int
    ) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ApplicationResponse.Data>() {

        override fun areItemsTheSame(
            oldItem: ApplicationResponse.Data,
            newItem: ApplicationResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ApplicationResponse.Data,
            newItem: ApplicationResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConnectionHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.all_application_item,
                parent,
                false
            ),
            mContext
        )
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        if (holder.itemView.player_view.player != null) {
            holder.itemView.player_view!!.player!!.playWhenReady = false
            holder.itemView.player_view!!.player!!.stop()
            holder.itemView.player_view!!.player!!.release()
            holder.itemView.player_view.player = null

        }
        super.onViewDetachedFromWindow(holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConnectionHolder -> {
                holder.bind(differ.currentList[position])
                holder.itemView.tvShortList.setOnClickListener {
                    mCallback.invoke(0)
                }
                holder.itemView.tvBlock.setOnClickListener {
                    mCallback.invoke(1)
                }
                holder.itemView.tvProfile.setOnClickListener {
                    mCallback.invoke(2)
                }
                holder.itemView.tvHeight.text = "Height: "+differ.currentList[position].heightFt+
                        "."+differ.currentList[position].heightIn+" ft"
                holder.itemView.tvAge.text = "Age: "+differ.currentList[position].age
                holder.itemView.tvUserName.text = differ.currentList[position].artistName
                if (!differ.currentList[position].video.isNullOrEmpty()) {
                    playVideo(
                        mContext, holder.itemView.player_view,
                        differ.currentList[position].video
                        /*"http://techslides.com/demos/sample-videos/small.mp4"*/
                    )
                }

                holder.itemView.imgPopUp.setOnClickListener {
                    if (holder.itemView.clBlockView.visibility == View.VISIBLE){
                        holder.itemView.clBlockView.visibility = View.GONE
                    }else{
                        holder.itemView.clBlockView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(projectResponse: List<ApplicationResponse.Data>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ApplicationResponse.Data) = with(itemView) {
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
            R.id.tvViewProfile -> {
                showToast(mContext, mContext.resources.getString(R.string.str_coming_soon))

            }
        }
    }
}