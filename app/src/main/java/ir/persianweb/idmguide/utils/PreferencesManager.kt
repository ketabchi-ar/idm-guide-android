package ir.persianweb.idmguide.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("idm_guide_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FONT_SIZE = "font_size_scale" // 1.0f, 1.2f, 1.4f
        private const val KEY_DARK_MODE = "dark_mode_enabled"
        private const val KEY_BOOKMARKS = "saved_bookmarks"
    }

    var fontSizeScale: Float
        get() = prefs.getFloat(KEY_FONT_SIZE, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_FONT_SIZE, value).apply()

    var isDarkModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    fun getBookmarks(): Set<String> {
        return prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
    }

    fun toggleBookmark(topicId: Int): Boolean {
        val current = getBookmarks().toMutableSet()
        val key = topicId.toString()
        val isAdded: Boolean
        if (current.contains(key)) {
            current.remove(key)
            isAdded = false
        } else {
            current.add(key)
            isAdded = true
        }
        prefs.edit().putStringSet(KEY_BOOKMARKS, current).apply()
        return isAdded
    }

    fun isBookmarked(topicId: Int): Boolean {
        return getBookmarks().contains(topicId.toString())
    }
}
