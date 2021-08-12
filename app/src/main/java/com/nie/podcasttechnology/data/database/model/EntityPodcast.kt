package com.nie.podcasttechnology.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nie.podcasttechnology.data.remote.model.PodcastItem
import com.nie.podcasttechnology.extension.xmlPubDateStrToDate
import java.io.Serializable
import java.util.*

@Entity(tableName = "Podcasts")
data class EntityPodcast(
    @PrimaryKey
    @ColumnInfo(name = "pubDate")
    val pubDate: Date,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "audioUrl")
    val audioUrl: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String
) : Serializable {
    companion object {
        fun from(item: PodcastItem) = EntityPodcast(
            pubDate = item.pubDate.xmlPubDateStrToDate(),
            title = item.title,
            description = item.description,
            type = item.enclosure.type,
            audioUrl = item.enclosure.audioUrl,
            imageUrl = item.image.imageUrl!!
        )
    }
}
