package id.dipoengoro.tryimagesearch.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import id.dipoengoro.tryimagesearch.R
import id.dipoengoro.tryimagesearch.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)

        binding.apply {
            val photo = args.photo

            Glide.with(this@DetailFragment)
                .load(photo.urls.regular)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressDetail.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressDetail.isVisible = false
                        textUsernameDetail.isVisible = true
                        textDescriptionDetail.isVisible = photo.description != null
                        return false
                    }
                })
                .into(imagePhotoDetail)

            textDescriptionDetail.text = photo.description

            val url = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, url)

            textUsernameDetail.apply {
                text = String.format(getString(R.string.attribution), photo.user.name)
                setOnClickListener {
                    requireContext().startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }
    }
}