package com.show.bannerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.show.banner.Banner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusBar {
            setFullScreen()
            lightBar(true)
        }

        val list = ArrayList<String>()
        list.apply {
            add("http://pic1.win4000.com/mobile/2020-10-14/5f86ad2a2f13d.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-14/5f86ad2b72406.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-14/5f86ad2dc6fb0.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-14/5f86ad2f184bc.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-14/5f86ad3017b70.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-14/5f86ad315bec2.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-14/5f86ad32a191b.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-13/5f8542cf38b9b.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-13/5f8542d032086.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-13/5f8542d13c96e.jpg")
            add("http://pic1.win4000.com/mobile/2020-10-13/5f8542d25a9c5.jpg")
        }

        val banner = findViewById<Banner>(R.id.banner)
        banner.addList(list)
        banner.setOnImageLoader { url, imageView ->
            Glide.with(imageView).load(url).into(imageView)
        }

        banner.bindToLife(this)

    }
}
