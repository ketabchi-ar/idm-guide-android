package ir.solard.idm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.solard.idm.R
import ir.solard.idm.data.model.TopicItem
import ir.solard.idm.databinding.ItemTopicCardBinding

class TopicAdapter(
    private var items: List<TopicItem>,
    private val onItemClick: (TopicItem) -> Unit
) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    fun updateList(newItems: List<TopicItem>) {
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

        fun bind(item: TopicItem) {
            binding.tvTitle.text = item.title

            val subtitleText = when (item.type) {
                "showForm" -> "آموزش متنی و تصویری"
                "showList" -> "شامل بخش‌های زیرمجموعه"
                "openUrl" -> "ارتباط و لینک"
                "exit" -> "خروج"
                else -> null
            }

            if (subtitleText != null && item.type != "exit") {
                binding.tvSubtitle.text = subtitleText
                binding.tvSubtitle.visibility = View.VISIBLE
            } else {
                binding.tvSubtitle.visibility = View.GONE
            }

            // Distinct icons depending on topic
            when {
                item.title.contains("معرفی") -> binding.ivIcon.setImageResource(R.drawable.ic_home)
                item.title.contains("امکانات") -> binding.ivIcon.setImageResource(R.drawable.ic_star)
                item.title.contains("تنظیم") || item.title.contains("آموزش") -> binding.ivIcon.setImageResource(R.drawable.ic_download)
                item.title.contains("تماس") -> binding.ivIcon.setImageResource(R.drawable.ic_share)
                item.title.contains("منابع") -> binding.ivIcon.setImageResource(R.drawable.ic_settings)
                item.title.contains("خروج") -> binding.ivIcon.setImageResource(R.drawable.ic_exit)
                else -> binding.ivIcon.setImageResource(R.drawable.ic_download)
            }

            binding.cardTopic.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
