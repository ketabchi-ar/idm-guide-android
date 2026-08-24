package ir.solard.idm.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ir.solard.idm.R
import ir.solard.idm.ads.AdManager
import ir.solard.idm.data.model.ArticleItem
import ir.solard.idm.databinding.ActivityDetailBinding
import ir.solard.idm.ui.adapter.StepAdapter
import ir.solard.idm.utils.PreferencesManager

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var prefs: PreferencesManager
    private var currentArticle: ArticleItem? = null

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferencesManager(this)
        currentArticle = intent.getSerializableExtra(EXTRA_ARTICLE) as? ArticleItem

        setupToolbar()
        setupContent()
        setupAds()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.topAppBarDetail)
        supportActionBar?.title = currentArticle?.title ?: getString(R.string.app_name)
        binding.topAppBarDetail.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupContent() {
        val article = currentArticle ?: return
        binding.rvDetailSteps.layoutManager = LinearLayoutManager(this)
        binding.rvDetailSteps.adapter = StepAdapter(article.steps)
    }

    private fun setupAds() {
        AdManager.showStandardBanner(this, binding.bannerAdContainerDetail)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        val bookmarkItem = menu?.findItem(R.id.action_bookmark)
        val articleId = currentArticle?.id ?: -1
        updateBookmarkIcon(bookmarkItem, prefs.isBookmarked(articleId))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                val articleId = currentArticle?.id ?: return false
                val isAdded = prefs.toggleBookmark(articleId)
                updateBookmarkIcon(item, isAdded)
                val msg = if (isAdded) getString(R.string.bookmark_added) else getString(R.string.bookmark_removed)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share_topic -> {
                val title = currentArticle?.title ?: ""
                val shareText = title + "\n\nراهنمای جامع IDM در کافه بازار"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "اشتراک"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateBookmarkIcon(item: MenuItem?, isBookmarked: Boolean) {
        if (isBookmarked) {
            item?.setIcon(R.drawable.ic_bookmark)
        } else {
            item?.setIcon(R.drawable.ic_bookmark_border)
        }
    }
}
