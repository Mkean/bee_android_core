package com.bee.core.glide

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.fragment.app.Fragment
import com.bee.core.utils.DeviceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation.CornerType
import java.io.ByteArrayOutputStream

/**
 * 图片工具类
 */
object ImageLoadingUtil {

    /**
     * 图片加载
     *
     * @param context    上下文
     * @param url        图片路径
     * @param resourceId 占位符
     * @param errorId    错误id
     * @param iv         目标View
     */
    fun loadImg(context: Context, url: String?, resourceId: Int, errorId: Int, iv: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv)
    }

    /**
     * 图片加载
     *
     * @param activity   activity
     * @param url        图片路径
     * @param resourceId 占位符
     * @param errorId    错误id
     * @param iv         目标View
     */
    fun loadImg(activity: Activity, url: String?, resourceId: Int, errorId: Int, iv: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
        Glide.with(activity)
                .load(url)
                .apply(options)
                .into(iv)
    }

    /**
     * 图片加载
     *
     * @param fragment   fragment
     * @param url        图片路径
     * @param resourceId 占位符
     * @param errorId    错误id
     * @param iv         目标View
     */
    fun loadImg(fragment: Fragment, url: String?, resourceId: Int, errorId: Int, iv: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
        Glide.with(fragment)
                .load(url)
                .apply(options)
                .into(iv)
    }

    /**
     * 图片加载
     *
     * @param view          view
     * @param url           图片路径
     * @param resourceId    占位符
     * @param errorId       错误id
     * @param iv            目标View
     */
    fun loadImg(view: View, url: String?, resourceId: Int, errorId: Int, iv: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
        Glide.with(view)
                .load(url)
                .apply(options)
                .into(iv)
    }

    /**
     * @param context   上下文
     * @param url       图片路径
     * @param iv        目标View
     */
    fun loadImg(context: Context, url: String?, iv: ImageView) {
        Glide.with(context)
                .load(url)
                .into(iv)
    }

    /**
     * @param activity   activity
     * @param url       图片路径
     * @param iv        目标View
     */
    fun loadImg(activity: Activity, url: String?, iv: ImageView) {
        Glide.with(activity)
                .load(url)
                .into(iv)
    }

    fun loadImg(context: Context, url: Uri, iv: ImageView) {
        Glide.with(context)
                .load(url)
                .into(iv)
    }

    fun loadImg(activity: Activity, url: Uri, iv: ImageView) {
        Glide.with(activity)
                .load(url)
                .into(iv)
    }

    fun loadImg(context: Context, resourceId: Int?, iv: ImageView) {
        Glide.with(context)
                .load(resourceId)
                .into(iv)
    }

    fun loadImg(activity: Activity, resourceId: Int?, iv: ImageView) {
        Glide.with(activity)
                .load(resourceId)
                .into(iv)
    }

    fun loadImg(view: View, resourceId: Int?, iv: ImageView) {
        Glide.with(view)
                .load(resourceId)
                .into(iv)
    }

    /**
     * 添加自定义options
     * @param context
     * @param url
     * @param options 自定义options
     * @param iv
     */
    fun loadImg(context: Context, url: String?, options: RequestOptions, iv: ImageView) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv)
    }

    fun loadImgForTag(context: Context, url: String?, tagKey: Int, defaultResId: Int, errorId: Int, iv: ImageView) {
        if (TextUtils.isEmpty(url)) {
            loadImg(context, url, iv)
        } else {
            iv.tag = tagKey
            Glide.with(context).load(url).placeholder(defaultResId).error(errorId).into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    val `object`: Any = iv.getTag(tagKey)
                    if (`object` is String) {
                        if (`object` == url) {
                            Glide.with(context).load(resource).format(DecodeFormat.PREFER_RGB_565).into(iv)
                        }
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Glide.with(context).load(errorDrawable).into(iv)
                }
            })
        }
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param resourceId
     * @param errorId
     * @param iv
     */
    fun loadImgForCircle(context: Context, url: String?, resourceId: Int, errorId: Int, iv: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
                .circleCrop()
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv)
    }

    fun loadImgForCircle(context: Context, bytes: ByteArray, iv: ImageView) {
        Glide.with(context)
                .load(bytes)
                .apply(RequestOptions().circleCrop())
                .into(iv)
    }

    fun loadImgForCircle(context: Context, url: String?, errorId: Int, imageView: ImageView) {
        val options = RequestOptions()
                .error(errorId)
                .circleCrop()
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView)
    }


    fun loadImgForCircle(view: View, url: String?, errorId: Int, imageView: ImageView) {
        val options = RequestOptions()
                .error(errorId)
                .circleCrop()
        Glide.with(view)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun loadImgForCircle(fragment: Fragment, url: String?, resourceId: Int, errorId: Int, imageView: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
                .circleCrop()
        Glide.with(fragment)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun loadImgForCircle(view: View, url: String?, resourceId: Int, errorId: Int, imageView: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
                .circleCrop()
        Glide.with(view)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun loadRoundedCorners(context: Fragment, url: String?, resourceId: Int, iv: ImageView, radius: Int, margin: Int) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(resourceId)
        when (iv.scaleType) {
            ImageView.ScaleType.CENTER_CROP -> options.transform(CenterCrop(), RoundedCornersTransformation(radius, margin))
            else -> options.transform(RoundedCornersTransformation(radius, margin))
        }

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv)
    }

    fun loadRoundedCorners(context: Context, url: String?, resourceId: Int, imageView: ImageView, radius: Int, margin: Int) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(resourceId)
        when (imageView.scaleType) {
            ScaleType.CENTER_CROP -> options.transform(CenterCrop(), RoundedCornersTransformation(radius, margin))
            else -> options.transform(RoundedCornersTransformation(radius, margin))
        }
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun loadRoundedCorners(view: View, url: String?, resourceId: Int, imageView: ImageView, radius: Int, margin: Int) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(resourceId)
        when (imageView.scaleType) {
            ScaleType.CENTER_CROP -> options.transform(CenterCrop(), RoundedCornersTransformation(radius, margin))
            else -> options.transform(RoundedCornersTransformation(radius, margin))
        }
        Glide.with(view)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    fun loadRoundedCorners(activity: Activity, url: String?, resourceId: Int, imageView: ImageView, cornerType: CornerType?, radius: Int, margin: Int) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(resourceId)
                .transform(RoundedCornersTransformation(radius, margin, cornerType))
        Glide.with(activity)
                .load(url)
                .apply(options)
                .into(imageView)
    }

    /**
     *加载GIF
     *低端手机直接加载resourceId
     * @param view
     * @param url
     * @param resourceId 低端手机加载资源 or 加载过程展位图
     * @param errorId
     * @param iv
     */
    fun loadImgGif(view: View, url: String?, resourceId: Int, errorId: Int, iv: ImageView) {
        val options = RequestOptions()
                .placeholder(resourceId)
                .error(errorId)
        if (DeviceUtil.isLowDevice()) {
            Glide.with(view)
                    .load(resourceId)
                    .apply(options)
                    .into(iv)
        } else {
            Glide.with(view)
                    .load(url)
                    .apply(options)
                    .into(iv)
        }
    }

    /**
     * 加载gif
     * 低端手机直接加载 resourceId资源
     *
     * @param view
     * @param url        gif地址
     * @param resourceId 低端手机加载资源
     * @param imageView
     */
    fun loadImg565Gif(view: View, url: String?, resourceId: Int, imageView: ImageView) {
        if (DeviceUtil.isLowDevice()) {
            Glide.with(view)
                    .load(resourceId)
                    .into(imageView)
        } else {
            Glide.with(view)
                    .load(url)
                    .into(imageView)
        }
    }

    /**
     * 加载gif
     * 低端手机直接加载 resourceId资源
     *
     * @param context
     * @param url        gif地址
     * @param resourceId 低端手机加载资源
     * @param imageView
     */
    fun loadImg565Gif(context: Context, url: String?, resourceId: Int, imageView: ImageView) {
        if (DeviceUtil.isLowDevice()) {
            Glide.with(context)
                    .load(resourceId)
                    .into(imageView)
        } else {
            Glide.with(context)
                    .load(url)
                    .into(imageView)
        }
    }

    /**
     * 清除图片占用的资源
     */
    fun clearViewCache(context: Context, view: View) {
        Glide.with(context).clear(view)
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    fun clearDiskCache(context: Context) {
        Glide.get(context).clearDiskCache()
    }

    /**
     * 清除尽可能多的内存
     *
     * @param context
     */
    fun onLowMemory(context: Context) {
        Glide.get(context).onLowMemory()
    }

    /**
     * 根据给定的级别清除一些具有确切数量的内存
     *
     * @param context
     * @param level
     */
    fun onTrimMemory(context: Context, level: Int) {
        Glide.get(context).onTrimMemory(level)
    }

    /**
     * @param context           上下文
     * @param url               图片url
     * @param defaultResourceId 默认资源id
     * @param errorId           加载失败时侯使用的图片资源id
     * @param imageView         展示图片的载体
     */
    fun loadImg565(context: Context, url: String?, defaultResourceId: Drawable, errorId: Drawable, imageView: ImageView) {
        Glide.with(context)
                .load(url)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(defaultResourceId)
                .error(errorId)
                .into(imageView)
    }

    fun loadImg565(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
                .load(url)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(imageView)
    }

    fun loadImg565ForCircle(context: Context, bytes: ByteArray?, imageView: ImageView) {
        Glide.with(context)
                .load(bytes)
                .format(DecodeFormat.PREFER_RGB_565)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imageView)
    }

    fun loadImg565ForCircle(context: Context, url: String?, errorId: Int, imageView: ImageView) {
        Glide.with(context)
                .load(url)
                .format(DecodeFormat.PREFER_RGB_565)
                .error(errorId)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imageView)
    }

    /**
     *获取图片的byte[]
     * @param context
     * @param url
     * @param resourceId
     * @param listener
     */
    fun getBytes(context: Context, url: String?, resourceId: Int, listener: IResourceListener?) {
        if (TextUtils.isEmpty(url)) return

        val options = RequestOptions()
                .placeholder(resourceId)
                .error(resourceId)
                .override(120)

        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions())
                .apply(options)
                .into(object : Target<Bitmap> {
                    override fun onLoadStarted(placeholder: Drawable?) {
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        if (listener != null) {
                            val resource = (errorDrawable as BitmapDrawable).bitmap

                            try {
                                ByteArrayOutputStream().use { outputStream ->
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                    listener.setBytes(outputStream.toByteArray())
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        if (listener != null) {
                            try {
                                ByteArrayOutputStream().use { outputStream ->
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                                    listener.setBytes(outputStream.toByteArray())
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun getSize(cb: SizeReadyCallback) {
                    }

                    override fun getRequest(): Request? = null

                    override fun onStop() {
                    }

                    override fun setRequest(request: Request?) {
                    }

                    override fun removeCallback(cb: SizeReadyCallback) {
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onStart() {
                    }

                    override fun onDestroy() {
                    }


                })
    }
}