package ir.solard.idm.data.repository

import android.content.Context
import com.google.gson.Gson
import ir.solard.idm.data.model.AppData
import ir.solard.idm.data.model.ArticleItem
import ir.solard.idm.data.model.CategoryItem
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
            AppData(emptyList(), emptyList())
        }
    }

    fun getCategories(): List<CategoryItem> {
        return getAppData().categories
    }

    fun getAllArticles(): List<ArticleItem> {
        return getAppData().articles
    }

    fun getArticlesByCategory(categoryId: String): List<ArticleItem> {
        if (categoryId.isEmpty() || categoryId == "all") {
            return getAllArticles()
        }
        return getAppData().articles.filter { it.categoryId == categoryId }
    }

    fun searchArticles(query: String): List<ArticleItem> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return getAllArticles()

        return getAppData().articles.filter { article ->
            article.title.lowercase().contains(trimmed) ||
            (article.subtitle?.lowercase()?.contains(trimmed) ?: false) ||
            article.steps.any { it.text.lowercase().contains(trimmed) }
        }
    }
}
