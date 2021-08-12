package com.nie.podcasttechnology.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.nie.podcasttechnology.R
import com.nie.podcasttechnology.data.database.model.EntityPodcast
import com.nie.podcasttechnology.databinding.ItemPodcastBinding
import com.nie.podcasttechnology.extension.throttleClick
import com.nie.podcasttechnology.extension.toDateString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class PodcastAdapter : RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder>() {

    private val items = mutableListOf<EntityPodcast>()

    private val compositeDisposable = CompositeDisposable()

    var onItemClicked = { _: EntityPodcast ->  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        val binding = ItemPodcastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PodcastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        holder.bind(items[position], compositeDisposable, onItemClicked)
    }

    override fun getItemCount() = items.size

    fun addAll(list: List<EntityPodcast>) {
        DiffUtil.calculateDiff(PodcastAdapterDiffCallback(items, list)).let {
            items.clear()
            items.addAll(list)
            it.dispatchUpdatesTo(this)
        }
    }

    fun clear() = compositeDisposable.clear()

    class PodcastViewHolder(private val binding: ItemPodcastBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EntityPodcast, compositeDisposable: CompositeDisposable, onItemClicked: (EntityPodcast) -> Unit) {
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