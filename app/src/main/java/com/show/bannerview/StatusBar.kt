package com.show.bannerview

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.*

fun FragmentActivity.statusBar(component : (Immerse.()->Unit)){
    component(Immerse.get())
}

fun Fragment.statusBar(component : (Immerse.()->Unit)){
    component(Immerse.get())
}

private var result = -1
fun Context.getStatusBarHeight(): Int {
    if(result == -1){
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}

class Immerse private constructor(){

    companion object{

        private val instant by lazy { Immerse() }

        fun get() = instant

        private var system = ""

        private val xiaomi = "xiaomi"
        private val oppo = "oppo"
        private val meizu = "meizu"

        private fun getSystem() : String{
            if(system.isEmpty()){
                system = Build.MANUFACTURER.toLowerCase(Locale.getDefault())
            }
            return system
        }

        private const val SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010

    }


    fun FragmentActivity.lightBar(light: Boolean = false) {
        when {
            getSystem() == meizu -> {
                setMeizuStatusBarDarkIcon(
                    this,
                    !light
                )
            }
            getSystem() == xiaomi -> {
                setMiuiStatusBarDarkMode(
                    this,
                    !light
                )
            }
            getSystem() == oppo -> {
                setOPPOStatusTextColor(
                    this,
                    !light
                )
            }
            else -> {
                android6StatusBarDarkMode(
                    this,
                    !light
                )
            }
        }
    }



    fun FragmentActivity.setFullScreen(toolbar: View) {
        setFullScreen()
        val params = toolbar.layoutParams
        params.height = (getStatusBarHeight() * 1)
        toolbar.layoutParams = params
    }



    fun Fragment.setFullScreen(toolbar: View) {
        val params = toolbar.layoutParams
        params.height = (activity?.getStatusBarHeight()?.times(1)!!)
        toolbar.layoutParams = params
    }


    /**
     * 顶部状态栏上移
     */
    fun FragmentActivity.setFullScreen() {
        setScreen()
        val mContentView = findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            mChildView.fitsSystemWindows = false
        }
    }


    private fun FragmentActivity.setScreen() {
        val window = window
        val decorView = window.decorView
        //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }


    fun FragmentActivity.setStatusColor(color: Int) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }


    fun FragmentActivity.setTranslucentNavigation() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }


    fun FragmentActivity.setTranslucentStatusBar() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    fun Activity.setTranslucent() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }



    private fun setOPPOStatusTextColor(activity: Activity, lightStatusBar: Boolean) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var vis = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vis = if (lightStatusBar) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vis = if (lightStatusBar) {
                vis or SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT
            } else {
                vis and SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT.inv()
            }
        }
        window.decorView.systemUiVisibility = vis
    }

    //3.对于flyme:
    //设置成白色的背景，字体颜色为黑色。
    private fun setMeizuStatusBarDarkIcon(activity: Activity?, dark: Boolean): Boolean {
        var result = false
        if (activity != null) {
            try {
                val lp = activity.window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                activity.window.attributes = lp
                result = true
            } catch (e: Exception) {
            }

        }
        return result
    }


    //2.对于miui
    //设置成白色的背景，字体颜色为黑色。
    private fun setMiuiStatusBarDarkMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true
                //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
            } catch (e: Exception) {
            }

        }
        return result
    }



    //设置成白色的背景，字体颜色为黑色。
    //需要在跟布局设置  android:fitsSystemWindows="true"
    //不设置上面的参数，布局会往上移
    private fun android6StatusBarDarkMode(activity: Activity, dark: Boolean) {
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }else{
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }



}


