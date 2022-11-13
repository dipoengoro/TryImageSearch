package id.dipoengoro.tryimagesearch

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val urls: PhotoUrls,
    val user: User
) : Parcelable {

    @Parcelize
    data class PhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class User(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=Try%20ImageSearch&utm_medium=referral"
    }
}