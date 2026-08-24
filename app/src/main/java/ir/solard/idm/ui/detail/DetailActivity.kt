package ir.solard.idm.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ir.solard.idm.R
import ir.solard.idm.ads.AdManager
import ir.solard.idm.data.model.TopicItem
import ir.solard.idm.databinding.ActivityDetailBinding
import ir.solard.idm.ui.adapter.StepAdapter
import ir.solard.idm.ui.adapter.TopicAdapter
import ir.solard.idm.utils.PreferencesManager

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var prefs: PreferencesManager
    private var currentTopic: TopicItem? = null

    companion object {
        const val EXTRA_TOPIC = "extra_topic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferencesManager(this)
        currentTopic = intent.getSerializableExtra(EXTRA_TOPIC) as? TopicItem

        setupToolbar()
        setupContent()
        setupAds()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.topAppBarDetail)
        supportActionBar?.title = currentTopic?.title ?: getString(R.string.app_name)
        binding.topAppBarDetail.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupContent() {
        val topic = currentTopic ?: return
        binding.rvDetailSteps.layoutManager = LinearLayoutManager(this)

        when (topic.type) {
            "showForm" -> {
                val pages = topic.contentPayload?.pages ?: emptyList()
                binding.rvDetailSteps.adapter = StepAdapter(pages)
            }
            "showList" -> {
                val subRows = topic.contentPayload?.subRows ?: emptyList()
                val convertedTopics = subRows.map { sub ->
                    TopicItem(
                        id = sub.id,
                        title = sub.title,
                        backColorOrImg = sub.backColorOrImg,
                        type = sub.type,
                        contentPayload = ir.solard.idm.data.model.ContentPayload(
                            pages = sub.payload?.pages,
                            url = sub.payload?.url,
                            mediaType = sub.payload?.mediaType
                        )
                    )
                }

                binding.rvDetailSteps.adapter = TopicAdapter(convertedTopics) { clickedSubTopic ->
                    if (clickedSubTopic.type == "MediaOnline" || clickedSubTopic.type == "openUrl") {
                        val targetUrl = clickedSubTopic.contentPayload?.url
                        if (!targetUrl.isNullOrEmpty()) {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl))
                            startActivity(browserIntent)
                        }
                    } else if (clickedSubTopic.type == "showForm") {
                        val detailIntent = Intent(this, DetailActivity::class.java).apply {
                            putExtra(EXTRA_TOPIC, clickedSubTopic)
                        }
                        startActivity(detailIntent)
                    }
                }
            }
            else -> {
                val pages = topic.contentPayload?.pages ?: emptyList()
                binding.rvDetailSteps.adapter = StepAdapter(pages)
            }
        }
    }

    private fun setupAds() {
        AdManager.showStandardBanner(this, binding.bannerAdContainerDetail)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        val bookmarkItem = menu?.findItem(R.id.action_bookmark)
        val topicId = currentTopic?.id ?: -1
        updateBookmarkIcon(bookmarkItem, prefs.isBookmarked(topicId))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                val topicId = currentTopic?.id ?: return false
                val isAdded = prefs.toggleBookmark(topicId)
                updateBookmarkIcon(item, isAdded)
                val msg = if (isAdded) getString(R.string.bookmark_added) else getString(R.string.bookmark_removed)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share_topic -> {
                val topicTitle = currentTopic?.title ?: ""
                val shareText = topicTitle + "\n\nراهنمای جامع IDM در کافه بازار"
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
