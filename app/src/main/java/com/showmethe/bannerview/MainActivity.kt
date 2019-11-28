package com.showmethe.bannerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.showmethe.banner.Banner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = ArrayList<String>()
        list.apply {
            add("http://image3.xyzs.com/upload/b9/40/1449104703418440/20151205/144925600471264_0.jpg")
            add("http://image1.xyzs.com/upload/9b/b2/1450314878985387/20151219/145046718515607_0.jpg")
            add("http://image2.xyzs.com/upload/fc/6a/1450315960904658/20151219/145046718037409_0.jpg")
            add("http://image4.xyzs.com/upload/03/f7/1449978315650125/20151217/145028981364776_0.jpg")
            add("http://image1.xyzs.com/upload/b2/88/1450055515760915/20151217/145028981024042_0.jpg")
            add("http://image2.xyzs.com/upload/f9/f0/1450057329342039/20151217/145028980312438_0.jpg")
            add("http://image1.xyzs.com/upload/6d/7a/1450150493535765/20151217/145028979798709_0.jpg")
        }

        val banner = findViewById<Banner>(R.id.banner)
        banner.addList(list)
        banner.setOnImageLoader { url, imageView ->
            Glide.with(imageView).load(url).into(imageView)
        }
        banner.play()
        banner.bindToLife(this)

    }
}
