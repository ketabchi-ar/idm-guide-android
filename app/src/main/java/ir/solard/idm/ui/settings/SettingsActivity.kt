package ir.solard.idm.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.solard.idm.IDMGuideApp
import ir.solard.idm.R
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

        setupThemeSelector()
        setupFontSizeSelector()
    }

    private fun setupThemeSelector() {
        when (prefs.themeMode) {
            "light" -> binding.rbThemeLight.isChecked = true
            "dark" -> binding.rbThemeDark.isChecked = true
            else -> binding.rbThemeSystem.isChecked = true
        }

        binding.rgTheme.setOnCheckedChangeListener { _, checkedId ->
            val selectedMode = when (checkedId) {
                R.id.rbThemeLight -> "light"
                R.id.rbThemeDark -> "dark"
                else -> "system"
            }
            if (prefs.themeMode != selectedMode) {
                prefs.themeMode = selectedMode
                IDMGuideApp.applyTheme(selectedMode)
            }
        }
    }

    private fun setupFontSizeSelector() {
        val currentScale = prefs.fontSizeScale
        binding.sliderFontSize.value = currentScale
        updateFontPreview(currentScale)

        binding.sliderFontSize.addOnChangeListener { _, value, _ ->
            prefs.fontSizeScale = value
            updateFontPreview(value)
        }
    }

    private fun updateFontPreview(scale: Float) {
        binding.tvFontPreview.textSize = 15f * scale
    }
}
