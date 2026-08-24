package ir.solard.idm.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppData(
    @SerializedName("categories") val categories: List<CategoryItem> = emptyList(),
    @SerializedName("articles") val articles: List<ArticleItem> = emptyList()
) : Serializable

data class CategoryItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("icon") val icon: String? = null,
    @SerializedName("description") val description: String? = null
) : Serializable

data class ArticleItem(
    @SerializedName("id") val id: Int,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("steps") val steps: List<StepItem> = emptyList()
) : Serializable

data class StepItem(
    @SerializedName("text") val text: String,
    @SerializedName("image") val image: String? = null
) : Serializable
