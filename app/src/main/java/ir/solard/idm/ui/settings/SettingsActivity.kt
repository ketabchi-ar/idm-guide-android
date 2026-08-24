package ir.solard.idm.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ir.solard.idm.databinding.ActivitySettingsBinding
import ir.solard.idm.utils.PreferencesManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferencesManager(this)

        binding.topAppBarSettings.setNavigationOnClickListener {
            finish()
        }

        // Dark mode setup
        binding.switchDarkMode.isChecked = prefs.isDarkModeEnabled
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.isDarkModeEnabled = isChecked
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Font size setup
        binding.sliderFontSize.value = prefs.fontSizeScale
        binding.sliderFontSize.addOnChangeListener { _, value, _ ->
            prefs.fontSizeScale = value
        }
    }
}
