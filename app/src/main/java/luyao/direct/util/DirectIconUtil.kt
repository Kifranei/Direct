package luyao.direct.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import luyao.direct.model.AppIconCache
import luyao.direct.model.entity.*
import luyao.ktx.ext.dp2px
import luyao.ktx.util.getCircleTextBitmap

/**
 * Description:
 * Author: luyao
 * Date: 2023/1/29 15:24
 */

//fun DirectEntity.loadIcon(view: ImageView) {
//   if (!localIcon.isNullOrEmpty()) {
//        val localIcon = Base64.decode(localIcon, Base64.DEFAULT)
//        Glide.with(view).load(localIcon).addListener(object : RequestListener<Drawable> {
//            override fun onLoadFailed(
//                e: GlideException?,
//                model: Any?,
//                target: Target<Drawable>?,
//                isFirstResource: Boolean
//            ): Boolean {
//                return true
//            }
//
//            override fun onResourceReady(
//                resource: Drawable?,
//                model: Any?,
//                target: Target<Drawable>?,
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//            ): Boolean {
//                resource?.let {
//                    view.setImageDrawable(it)
//                    AppIconCache.put("${packageName}${label}", it.toBitmap())
//                }
//                return true
//            }
//        }).into(view)
//    } else if (!iconUrl.isNullOrEmpty()) {
//        Glide.with(view).load(iconUrl).addListener(object : RequestListener<Drawable> {
//            override fun onLoadFailed(
//                e: GlideException?,
//                model: Any?,
//                target: Target<Drawable>?,
//                isFirstResource: Boolean
//            ): Boolean {
//                return true
//            }
//
//            override fun onResourceReady(
//                resource: Drawable?,
//                model: Any?,
//                target: Target<Drawable>?,
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//            ): Boolean {
//                resource?.let {
//                    view.setImageDrawable(it)
//                    AppIconCache.put("${packageName}${label}", it.toBitmap())
//                }
//                return true
//            }
//        }).into(view)
//    } else if (packageName.isNotEmpty()) {
//        AppIconCache.setImageViewBitmap(packageName, label, view)
//    } else {
//        val circleTextBitmap = getCircleTextBitmap(label.substring(0, 1), dp2px(40).toInt())
//        AppIconCache.put("${packageName}${label}", circleTextBitmap)
//        Glide.with(view).load(circleTextBitmap)
//            .into(view)
//    }
//}

private fun NewDirectEntity.loadIconResource(view: ImageView, resource: Any, fallback: android.graphics.Bitmap) {
    Glide.with(view).load(resource).addListener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            resource?.let {
                AppIconCache.put("${packageName}${label}", it.toBitmap())
            }
            return false
        }
    }).placeholder(BitmapDrawable(view.resources, fallback))
        .error(BitmapDrawable(view.resources, fallback))
        .into(view)
}

fun NewDirectEntity.loadIcon(view: ImageView) {
    Glide.with(view).clear(view)
    val viewWidth = if (view.width > 0) view.width else dp2px(40).toInt()
    val fallback = AppIconCache.getImageBitmap(packageName, label, viewWidth)
    view.setImageBitmap(fallback)

    if (!localIcon.isNullOrEmpty()) {
        runCatching { Base64.decode(localIcon, Base64.DEFAULT) }
            .onSuccess { loadIconResource(view, it, fallback) }
    } else if (!iconUrl.isNullOrEmpty()) {
        loadIconResource(view, iconUrl, fallback)
    }
}

fun SimpleAppEntity.loadIcon(view: ImageView) {
    AppIconCache.setImageViewBitmap(packageName, "", view)
}

fun AppDirect.loadIcon(view: ImageView) {
    AppIconCache.setImageViewBitmap(packageName, "", view)
}

fun AppEntity.loadIcon(view: ImageView) {
    AppIconCache.setImageViewBitmap(packageName, "", view)
}

fun DirectCount.loadIcon(view: ImageView) {
    AppIconCache.setImageViewBitmap(packageName, "", view)
}

fun RecentEntity.loadIcon(view: ImageView) {
    AppIconCache.setImageViewBitmap(packageName, name, view)
}
