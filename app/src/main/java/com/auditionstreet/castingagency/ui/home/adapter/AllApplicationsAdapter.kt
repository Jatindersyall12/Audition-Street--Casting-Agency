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
import com.auditionstreet.castingagency.model.response.MyProjectResponse
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
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.all_application_item.view.*
import java.io.File

@Suppress("DEPRECATION")
class AllApplicationsAdapter(
    val mContext: FragmentActivity, private val mCallback: (
        mposition: Int
    ) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyProjectResponse.Data>() {

        override fun areItemsTheSame(
            oldItem: MyProjectResponse.Data,
            newItem: MyProjectResponse.Data
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MyProjectResponse.Data,
            newItem: MyProjectResponse.Data
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
                prePair(
                    holder.itemView.player_view,
                    "content://com.android.providers.media.documents/document/video%3A327469"
                )
            }
        }
    }

    private fun prePair(playerView: PlayerView, url: String) {
        Log.e("url", url)
        var proxyUrl = url
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, 16))
            .setBufferDurationsMs(1 * 1024, 1 * 1024, 500, 1024)
            .setTargetBufferBytes(-1)
            .setPrioritizeTimeOverSizeThresholds(true)
            .createDefaultLoadControl()
        val trackSelector = DefaultTrackSelector()
        var player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl)
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            mContext,
            Util.getUserAgent(mContext, mContext.getResources().getString(R.string.app_name))
        )
        /*val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(proxyUrl))*/
        val p = File(proxyUrl)
        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.fromFile(p))

        //RawResourceDataSource.buildRawResourceUri(/*R.raw.video1*/rawFile)
        //player.addListener(this)
        playerView.setPlayer(player)
        player.seekTo(0, 0)
        player.setRepeatMode(Player.REPEAT_MODE_ALL)
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
        player.prepare(videoSource)
        player.setPlayWhenReady(true)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(projectResponse: List<MyProjectResponse.Data>) {
        differ.submitList(projectResponse)
        notifyDataSetChanged()
    }

    class ConnectionHolder(
        itemView: View,
        val mContext: Context
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MyProjectResponse.Data) = with(itemView) {
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