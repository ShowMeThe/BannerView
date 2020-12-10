package com.show.banner.util

import android.content.Context

/**
 *  com.showmethe.banner.util
 *  2019/11/28
 *  21:13
 */
fun dip2px(context: Context, dipValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun px2dip(context: Context, pxValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f)
}