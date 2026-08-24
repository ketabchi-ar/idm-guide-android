package ir.solard.idm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.solard.idm.R
import ir.solard.idm.data.model.ArticleItem
import ir.solard.idm.databinding.ItemTopicCardBinding

class TopicAdapter(
    private var items: List<ArticleItem>,
    private val onItemClick: (ArticleItem) -> Unit
) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    fun updateList(newItems: List<ArticleItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopicCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemTopicCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ArticleItem) {
            binding.tvTitle.text = item.title

            if (!item.subtitle.isNullOrEmpty()) {
                binding.tvSubtitle.text = item.subtitle
                binding.tvSubtitle.visibility = View.VISIBLE
            } else {
                binding.tvSubtitle.visibility = View.GONE
            }

            // Distinct icons depending on category
            val iconRes = when (item.categoryId) {
                "intro" -> R.drawable.ic_home
                "features" -> R.drawable.ic_star
                "tutorials" -> R.drawable.ic_download
                "tips" -> R.drawable.ic_settings
                "shortcuts" -> R.drawable.ic_search
                "info" -> R.drawable.ic_share
                else -> R.drawable.ic_download
            }
            binding.ivIcon.setImageResource(iconRes)

            binding.cardTopic.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
