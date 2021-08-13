package com.nie.podcasttechnology.data.remote.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

@Root(name = "rss", strict = false)
data class Rss(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val channel: Channel
)

@Root(name = "channel", strict = false)
data class Channel(
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    val title: String,
    @field:Element(name = "pubDate", required = false)
    @param:Element(name = "pubDate", required = false)
    val pubDate: String,
    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    val description: String,
    @field:ElementList(entry = "image", inline = true, required = false)
    @param:ElementList(entry = "image", inline = true, required = false)
    val image: List<Image>,
    @field:ElementList(entry = "item", inline = true, required = false)
    @param:ElementList(entry = "item", inline = true, required = false)
    val items: List<PodcastItem>
)

@Root(name = "item", strict = false)
data class PodcastItem(
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    val title: String,
    @field:Element(name = "pubDate", required = false)
    @param:Element(name = "pubDate", required = false)
    val pubDate: String,
    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    val description: String,
    @field:Element(name = "duration", required = false)
    @param:Element(name = "duration", required = false)
    val duration: String,
    @field:Element(name = "enclosure", required = false)
    @param:Element(name = "enclosure", required = false)
    val enclosure: Enclosure,
    @field:Element(name = "image", required = false)
    @param:Element(name = "image", required = false)
    val image: Image
) : Serializable

@Root(name = "enclosure", strict = false)
data class Enclosure(
    @field:Attribute(name = "type", required = false)
    @param:Attribute(name = "type", required = false)
    val type: String,
    @field:Attribute(name = "url", required = false)
    @param:Attribute(name = "url", required = false)
    val audioUrl: String
) : Serializable

@Root(name = "image", strict = false)
data class Image(
    @field:Attribute(name = "href", required = false)
    @param:Attribute(name = "href", required = false)
    val imageUrl: String?,
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    val title: String?,
    @field:Element(name = "link", required = false)
    @param:Element(name = "link", required = false)
    val link: String?
) : Serializable