package com.example.shortvideos.adapter

import android.content.Context
import android.location.GnssAntennaInfo.Listener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shortvideos.R
import com.example.shortvideos.model.Hit
import com.example.shortvideos.utils.Util.Companion.TAG

class PagerAdapter(private val context: Context)
    :RecyclerView.Adapter<PagerAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var videoPlayer: PlayerView = itemView.findViewById(R.id.videoPlayer)
         var player:ExoPlayer? = null
         var currentPlayer:ExoPlayer? = null

         fun initializePlayer(url:MediaItem){
             try {
                     releasePlayer()
                     player = ExoPlayer.Builder(context).build()
                         .also {
                             videoPlayer.player = it
                             currentPlayer = it
                         }
                     player?.setMediaItem(url)
                     player?.playWhenReady = true
                     player?.prepare()
             }catch (e:Exception){
                 Log.e(TAG,e.message.toString())
             }
        }

         fun releasePlayer() {
            if (player != null) {
                player?.release()
                Log.e(TAG,"Player Release")
                player = null
            }
        }

    }

    private val diffUtilCallBack = object :DiffUtil.ItemCallback<Hit>(){
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }
    }

    //diff util to find difference between two list to efficiently update layout
    val diff = AsyncListDiffer(this,diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.video_view,parent,false)
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val item = diff.currentList[position]
        val mediaItem = MediaItem.fromUri(item.videos.small.url)

        //calling stop for previous player
        holder.currentPlayer.let {
                Log.e("PagerAdapter","stop called")
                it?.stop()
            }


        if (holder.player?.isPlaying == true){
            Log.e(TAG,"Playing")
            holder.releasePlayer()
            holder.initializePlayer(mediaItem)
        }else{
            Log.e(TAG,"Empty")
            holder.releasePlayer()
            holder.initializePlayer(mediaItem)
        }

    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
        Log.e(TAG,"ViewRecycled")
    }


}