package ir.persianweb.idmguide.ui.bookmarks

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ir.persianweb.idmguide.data.repository.ContentRepository
import ir.persianweb.idmguide.databinding.ActivityBookmarksBinding
import ir.persianweb.idmguide.ui.adapter.TopicAdapter
import ir.persianweb.idmguide.ui.detail.DetailActivity
import ir.persianweb.idmguide.utils.PreferencesManager

class BookmarksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarksBinding
    private lateinit var repo: ContentRepository
    private lateinit var prefs: PreferencesManager
    private lateinit var adapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = ContentRepository(this)
        prefs = PreferencesManager(this)

        binding.topAppBarBookmarks.setNavigationOnClickListener {
            finish()
        }

        binding.rvBookmarks.layoutManager = LinearLayoutManager(this)
        adapter = TopicAdapter(emptyList()) { topic ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_TOPIC, topic)
            }
            startActivity(intent)
        }
        binding.rvBookmarks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadBookmarks()
    }

    private fun loadBookmarks() {
        val bookmarkedIds = prefs.getBookmarks()
        val allTopics = repo.getAppData().rows
        val bookmarkedTopics = allTopics.filter { bookmarkedIds.contains(it.id.toString()) }

        if (bookmarkedTopics.isEmpty()) {
            binding.tvEmptyBookmarks.visibility = View.VISIBLE
            binding.rvBookmarks.visibility = View.GONE
        } else {
            binding.tvEmptyBookmarks.visibility = View.GONE
            binding.rvBookmarks.visibility = View.VISIBLE
            adapter.updateList(bookmarkedTopics)
        }
    }
}
