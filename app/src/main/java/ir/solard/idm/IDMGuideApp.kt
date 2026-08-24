package ir.solard.idm

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ir.solard.idm.ads.AdManager
import ir.solard.idm.utils.PreferencesManager

class IDMGuideApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Preferences & Dark Mode
        val prefs = PreferencesManager(this)
        if (prefs.isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        // Initialize Ads SDK (Tapsell Plus)
        AdManager.initialize(this)
    }
}
