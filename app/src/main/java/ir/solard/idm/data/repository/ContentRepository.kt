package ir.solard.idm.data.repository

import android.content.Context
import com.google.gson.Gson
import ir.solard.idm.data.model.AppData
import ir.solard.idm.data.model.TopicItem
import java.io.InputStreamReader

class ContentRepository(private val context: Context) {

    private val gson = Gson()
    private var cachedData: AppData? = null

    fun getAppData(): AppData {
        cachedData?.let { return it }

        return try {
            val inputStream = context.assets.open("data.json")
            val reader = InputStreamReader(inputStream, "UTF-8")
            val data = gson.fromJson(reader, AppData::class.java)
            reader.close()
            cachedData = data
            data
        } catch (e: Exception) {
            e.printStackTrace()
            AppData(emptyList())
        }
    }

    fun getTopicById(id: Int): TopicItem? {
        return getAppData().rows.find { it.id == id }
    }

    fun searchTopics(query: String): List<TopicItem> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return emptyList()

        return getAppData().rows.filter { topic ->
            if (topic.title.lowercase().contains(trimmed)) return@filter true
            // Search inside steps/pages
            val hasPageMatch = topic.contentPayload?.pages?.any { it.text.lowercase().contains(trimmed) } ?: false
            if (hasPageMatch) return@filter true

            // Search inside subrows
            val hasSubRowMatch = topic.contentPayload?.subRows?.any { subRow ->
                subRow.title.lowercase().contains(trimmed) ||
                (subRow.payload?.pages?.any { it.text.lowercase().contains(trimmed) } ?: false)
            } ?: false

            hasSubRowMatch
        }
    }
}
