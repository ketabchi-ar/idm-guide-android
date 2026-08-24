package ir.persianweb.idmguide.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.persianweb.idmguide.R
import ir.persianweb.idmguide.data.model.PageStep
import ir.persianweb.idmguide.databinding.ItemDetailStepBinding
import ir.persianweb.idmguide.utils.PreferencesManager
import java.io.InputStream

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

            // Adjust font size based on settings
            val baseSize = 15f
            binding.tvStepText.textSize = baseSize * prefs.fontSizeScale
            binding.tvStepText.text = step.text

            // Load Image from assets if available
            if (!step.image.isNullOrEmpty()) {
                binding.ivStepImage.visibility = View.VISIBLE
                try {
                    val assetPath = "file:///android_asset/${step.image}"
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

            // Copy Action
            binding.btnCopy.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("IDM Guide", step.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
            }

            // Share Action
            binding.btnShare.setOnClickListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${step.text}

(از طریق برنامه راهنمای جامع IDM)")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "اشتراک‌گذاری متن")
                context.startActivity(shareIntent)
            }
        }
    }
}
