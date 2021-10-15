package com.nie.podcasttechnology.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.data.ui.ViewEpisode
import com.nie.podcasttechnology.databinding.ItemPodcastBinding
import com.nie.podcasttechnology.extension.addTo
import com.nie.podcasttechnology.extension.throttleClick
import com.nie.podcasttechnology.extension.toDateString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class EpisodeAdapter : PagingDataAdapter<ViewEpisode, EpisodeAdapter.PodcastViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ViewEpisode>() {
            override fun areItemsTheSame(oldItem: ViewEpisode, newItem: ViewEpisode): Boolean {
                return oldItem.pubDate.time == newItem.pubDate.time
            }

            override fun areContentsTheSame(oldItem: ViewEpisode, newItem: ViewEpisode): Boolean {
                return oldItem.pubDate.time == newItem.pubDate.time
            }
        }
    }

    private val compositeDisposable = CompositeDisposable()

    var onItemClicked = { _: ViewEpisode ->  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        val binding = ItemPodcastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PodcastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, compositeDisposable, onItemClicked)
        }
    }

    fun clear() = compositeDisposable.clear()

    class PodcastViewHolder(private val binding: ItemPodcastBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ViewEpisode, compositeDisposable: CompositeDisposable, onItemClicked: (ViewEpisode) -> Unit) {
            RxView.clicks(binding.root)
                .throttleClick()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onItemClicked.invoke(item)
                }.addTo(compositeDisposable)

            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.place_holder_grey)
                .into(binding.imagePodcastCover)

            binding.textTitle.text = item.title
            binding.textDate.text = item.pubDate.toDateString()
        }
    }
}