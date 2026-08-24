package ir.solard.idm.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import ir.solard.idm.R
import ir.solard.idm.ads.AdManager
import ir.solard.idm.data.model.ArticleItem
import ir.solard.idm.data.repository.ContentRepository
import ir.solard.idm.databinding.ActivityMainBinding
import ir.solard.idm.ui.adapter.TopicAdapter
import ir.solard.idm.ui.bookmarks.BookmarksActivity
import ir.solard.idm.ui.detail.DetailActivity
import ir.solard.idm.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: ContentRepository
    private lateinit var adapter: TopicAdapter
    private var selectedCategoryId: String = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = ContentRepository(this)

        setupViews()
        setupDrawer()
        setupCategoryChips()
        setupSearch()
        setupAds()
    }

    private fun setupViews() {
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.rvTopics.layoutManager = LinearLayoutManager(this)
        adapter = TopicAdapter(repository.getAllArticles()) { article ->
            handleArticleClick(article)
        }
        binding.rvTopics.adapter = adapter
    }

    private fun setupCategoryChips() {
        binding.chipGroupCategories.removeAllViews()

        // "All" chip
        val allChip = Chip(this).apply {
            text = "همه بخش‌ها"
            isCheckable = true
            isChecked = true
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedCategoryId = "all"
                    filterArticles()
                }
            }
        }
        binding.chipGroupCategories.addView(allChip)

        // Add category chips from data
        val categories = repository.getCategories()
        for (category in categories) {
            val chip = Chip(this).apply {
                text = category.title
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedCategoryId = category.id
                        filterArticles()
                    }
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun filterArticles() {
        val query = binding.etSearch.text?.toString().orEmpty()
        var list = repository.getArticlesByCategory(selectedCategoryId)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                (it.subtitle?.lowercase()?.contains(q) ?: false) ||
                it.steps.any { step -> step.text.lowercase().contains(q) }
            }
        }
        adapter.updateList(list)
    }

    private fun handleArticleClick(article: ArticleItem) {
        AdManager.showInterstitialAd(this) {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_ARTICLE, article)
            }
            startActivity(intent)
        }
    }

    private fun setupDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    selectedCategoryId = "all"
                    setupCategoryChips()
                    adapter.updateList(repository.getAllArticles())
                    true
                }
                R.id.nav_bookmarks -> {
                    startActivity(Intent(this, BookmarksActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.nav_rate -> {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(getString(R.string.bazaar_package_url))
                            setPackage("com.farsitel.bazaar")
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(this, "برنامه کافه بازار یافت نشد", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.nav_share -> {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "دانلود برنامه راهنمای جامع اینترنت دانلود منیجر IDM از کافه بازار:\nhttps://cafebazaar.ir/app/ir.solard.idm"
                        )
                        type = "text/plain"
                    }
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
                    true
                }
                R.id.nav_exit -> {
                    showExitDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterArticles()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupAds() {
        AdManager.showStandardBanner(this, binding.bannerAdContainer)
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_exit_title)
            .setMessage(R.string.dialog_exit_message)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}
