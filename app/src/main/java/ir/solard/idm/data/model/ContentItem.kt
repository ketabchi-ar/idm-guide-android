package ir.solard.idm.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppData(
    @SerializedName("rows") val rows: List<TopicItem> = emptyList()
) : Serializable

data class TopicItem(
    @SerializedName("id") val id: Int,
    @SerializedName("text") val title: String,
    @SerializedName("back") val backColorOrImg: String? = null,
    @SerializedName("etype") val type: String, // showForm, showList, openUrl, exit
    @SerializedName("e") val contentPayload: ContentPayload? = null
) : Serializable

data class ContentPayload(
    @SerializedName("page") val pages: List<PageStep>? = null,
    @SerializedName("rows") val subRows: List<SubTopicItem>? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("MediaType") val mediaType: String? = null
) : Serializable

data class PageStep(
    @SerializedName("txt") val text: String,
    @SerializedName("img") val image: String? = null,
    @SerializedName("share") val share: String? = null
) : Serializable

data class SubTopicItem(
    @SerializedName("id") val id: Int,
    @SerializedName("text") val title: String,
    @SerializedName("back") val backColorOrImg: String? = null,
    @SerializedName("etype") val type: String, // MediaOnline, showForm, cell, openUrl
    @SerializedName("e") val payload: SubContentPayload? = null
) : Serializable

data class SubContentPayload(
    @SerializedName("url") val url: String? = null,
    @SerializedName("MediaType") val mediaType: String? = null,
    @SerializedName("page") val pages: List<PageStep>? = null,
    @SerializedName("cells") val cells: List<CellItem>? = null
) : Serializable

data class CellItem(
    @SerializedName("id") val id: Int,
    @SerializedName("text") val title: String,
    @SerializedName("back") val image: String? = null,
    @SerializedName("etype") val type: String,
    @SerializedName("e") val payload: CellPayload? = null
) : Serializable

data class CellPayload(
    @SerializedName("url") val url: String? = null
) : Serializable
