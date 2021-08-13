package com.nie.podcasttechnology.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.data.database.model.EntityEpisode
import com.nie.podcasttechnology.databinding.ItemPodcastBinding
import com.nie.podcasttechnology.extension.throttleClick
import com.nie.podcasttechnology.extension.toDateString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class EpisodeAdapter : PagingDataAdapter<EntityEpisode, EpisodeAdapter.PodcastViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<EntityEpisode>() {
            override fun areItemsTheSame(oldItem: EntityEpisode, newItem: EntityEpisode): Boolean {
                return oldItem.pubDate.time == newItem.pubDate.time
            }

            override fun areContentsTheSame(oldItem: EntityEpisode, newItem: EntityEpisode): Boolean {
                return oldItem.pubDate.time == newItem.pubDate.time
            }
        }
    }

    private val compositeDisposable = CompositeDisposable()

    var onItemClicked = { _: EntityEpisode ->  }

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

        fun bind(item: EntityEpisode, compositeDisposable: CompositeDisposable, onItemClicked: (EntityEpisode) -> Unit) {
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