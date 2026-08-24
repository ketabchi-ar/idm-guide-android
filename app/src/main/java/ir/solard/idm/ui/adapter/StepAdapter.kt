package ir.solard.idm.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.solard.idm.R
import ir.solard.idm.data.model.PageStep
import ir.solard.idm.databinding.ItemDetailStepBinding
import ir.solard.idm.utils.PreferencesManager

class StepAdapter(
    private val steps: List<PageStep>
) : RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size

    inner class ViewHolder(private val binding: ItemDetailStepBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(step: PageStep) {
            val context = binding.root.context
            val prefs = PreferencesManager(context)

            val baseSize = 15f
            binding.tvStepText.textSize = baseSize * prefs.fontSizeScale
            binding.tvStepText.text = step.text

            if (!step.image.isNullOrEmpty()) {
                binding.ivStepImage.visibility = View.VISIBLE
                try {
                    val assetPath = "file:///android_asset/" + step.image
                    Glide.with(context)
                        .load(assetPath)
                        .into(binding.ivStepImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.ivStepImage.visibility = View.GONE
                }
            } else {
                binding.ivStepImage.visibility = View.GONE
            }

            binding.btnCopy.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("IDM Guide", step.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
            }

            binding.btnShare.setOnClickListener {
                val shareBody = step.text + "\n\n(از طریق برنامه راهنمای جامع IDM)"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareBody)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "اشتراک‌گذاری متن")
                context.startActivity(shareIntent)
            }
        }
    }
}
