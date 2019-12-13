### 基于ViewPager2的Banner

#### 图片GIF</br>
<img src ="https://github.com/ShowMeThe/BannerView/blob/master/jpg/20191213.gif" alt = "GIF"/></br>

#### XML写法</br>

```java
<com.showmethe.banner.Banner
        android:id="@+id/banner"
        android:transitionName="photo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:autoPlay="true"
        app:imageMaxHeight="1200dp"
        app:imageMinHeight="600dp"
        app:imageScaleType="centerCrop"
        app:indicator_gravity="CENTER"
        app:scrollType="INFINITE"
        app:dotType="RECTANGLE"
        app:dotWith="12dp"
        />
       
```
</br>

#### 绑定生命周期DefaultLifecycleObserver 
```java

 override fun onStop(owner: LifecycleOwner) {
        stopPlay()
    }

    override fun onResume(owner: LifecycleOwner) {
        if (autoPlay) {
            resumePlay()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stopPlay()
        this.owner?.lifecycle?.removeObserver(this)
        this.owner = null
    }

```
</br>
可以利用以下代码处理DefaultLifecycleObserver 版本问题
</br>

```java

configurations{
    all{
        resolutionStrategy{
            eachDependency{  DependencyResolveDetails details ->
                if(details.requested.name == 'XXXX'){
                    details.useTarget group: 'XXXX',name:'XXXX',version:"XXX{Version}"
                }
            }
        }
    }
}

```
</br>

#### style.xml

```java
 <declare-styleable name="Banner">
        <attr name="selected_color" format="color|reference"/>
        <attr name="unselected_color" format="color|reference"/>
        <attr name="autoPlay" format="boolean"/>
        <attr name="dotWith" format="dimension"/>
        <attr name="dotDistant" format="dimension"/>
        <attr name="dotType" format="enum">
            <enum name="OVAL" value="0"/>
            <enum name="RECTANGLE" value="1"/>
        </attr>
        <attr name="delayTime" format="integer"/>
        <attr name="pageOrientation" format="enum">
            <enum name="HORIZONTAL" value="0"/>
        </attr>
        <attr name="transformer" format="enum">
            <enum name="Parallax" value="0"/>
            <enum name="Spinner" value="1"/>
            <enum name="AlphaScale" value="2"/>
        </attr>
        <attr name="imageScaleType" format="enum">
            <enum name="fitXY" value="0"/>
            <enum name="centerCrop" value="1"/>
        </attr>
        <attr name="showIndicator" format="boolean"/>
        <attr name="imageMinHeight" format="dimension"/>
        <attr name="imageMaxHeight" format="dimension"/>
        <attr name="indicator_gravity" format="enum">
            <enum name="CENTER" value="0"/>
            <enum name="START" value="1"/>
            <enum name="END" value="2"/>
        </attr>
        <attr name="scrollType" format="enum">
            <enum name="REPEAT" value="3"/>
            <enum name="INFINITE" value="4"/>
        </attr>
    </declare-styleable>


```
</br>

 参数 | 类型 | 作用
 ---- | ----- | ------  
 selected_color | 颜色 | 选中指示条的颜色
 unselected_color | 颜色 | 未选中指示条的颜色
 autoPlay | 布尔 | 是否自动播放
 dotWith | 尺寸 | 指指示条的大小
 dotDistant | 尺寸 | 指示条的相隔距离
 dotType | 枚举 | 指示条的类型：OVAL和RECTANGLE
 delayTime | 时长 | 切换的延时
 pageOrientation | 枚举 | 滑动方向，暂时为水平
 transformer | 枚举 | 切换的动画
 imageScaleType | 枚举 | 图片适配类型
 showIndicator | 布尔 | 是否显示指示条
 scrollType | 枚举 | 轮播类型
