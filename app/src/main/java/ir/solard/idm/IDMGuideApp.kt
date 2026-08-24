package ir.solard.idm

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ir.solard.idm.ads.AdManager
import ir.solard.idm.utils.PreferencesManager

class IDMGuideApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val prefs = PreferencesManager(this)
        applyTheme(prefs.themeMode)

        AdManager.initialize(this)
    }

    companion object {
        fun applyTheme(themeMode: String) {
            when (themeMode) {
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}
