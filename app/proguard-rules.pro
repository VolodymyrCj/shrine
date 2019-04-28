# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# DJI SDK
-keepattributes Exceptions,InnerClasses,*Annotation*,Signature,EnclosingMethod

-dontwarn okio.**
-dontwarn org.bouncycastle.**
-dontwarn dji.**
-dontwarn com.dji.**

-keepclassmembers enum * {
    public static <methods>;
}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep,allowshrinking class * extends dji.publics.DJIUI.** {
    public <methods>;
}

-keep class net.sqlcipher.** { *; }

-keep class net.sqlcipher.database.* { *; }

-keep class dji.** { *; }

-keep class com.dji.** { *; }

-keep class com.google.** { *; }

-keep class org.bouncycastle.** { *; }

-keep,allowshrinking class org.** { *; }

-keep class com.squareup.wire.** { *; }

-keep class sun.misc.Unsafe { *; }

-keep class com.secneo.** { *; }

-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class android.support.v7.widget.SearchView { *; }

-keepclassmembers class * extends android.app.Service
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keep class android.support.** { *; }
-keep class android.media.** { *; }
-keep class okio.** { *; }

# MQtt: paho.mqtt.android
-keep class org.eclipse.paho.** {*;}
-keep interface org.eclipse.paho.** {*;}
-keep enum org.eclipse.paho.** {*;}
-dontwarn org.eclipse.paho.**

# MQtt: mqtt-client
#-keep class org.fusesource.** {*;}
#-keep interface org.fusesource.** {*;}
#-keep enum org.fusesource.** {*;}
#-keep class org.osgi.** {*;}
#-keep interface org.osgi.** {*;}
#-keep enum org.osgi.** {*;}
-keep class javax.** {*;}
-keep interface javax.** {*;}
-keep enum javax.** {*;}
#-keep class org.objectweb.** {*;}
#-keep interface org.objectweb.** {*;}
#-keep enum org.objectweb.** {*;}
#-dontwarn org.fusesource.**
#-dontwarn org.osgi.**
-dontwarn javax.**
#-dontwarn org.objectweb.**

#Butterknife
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

# 高德
##3D 地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.api.trace.**{*;}
-dontwarn com.amap.api.maps.**
-dontwarn com.amap.apis.**
##定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
##搜索
-keep class com.amap.api.services.**{*;}
##2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
##导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

# 二维码
-keep class cn.bingoogolapple.qrcode.** {*;}
-keep interface cn.bingoogolapple.qrcode.** {*;}
-keep enum cn.bingoogolapple.qrcode.** {*;}

# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#ProtoBuf
#-keep class com.google.protobuf.** {*;}
#-keep interface com.google.protobuf.** {*;}
#-keep enum com.google.protobuf.** {*;}
#-keep class google.protobuf.** {*;}
#-keep interface google.protobuf.** {*;}
#-keep enum google.protobuf.** {*;}
#-keep class sun.misc.** {*;}
#-keep interface sun.misc.** {*;}
#-keep enum sun.misc.** {*;}
#-dontwarn sun.misc.**

#retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#okio
-dontwarn okio.**
#picasso
-dontwarn com.squareup.okhttp.**
#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
-dontwarn com.bumptech.glide.load.resource.bitmap.Downsampler
-dontwarn com.bumptech.glide.load.resource.bitmap.HardwareConfigState
#ijkplayer
#-keep class tv.danmaku.ijk.media.** {*;}
#-keep interface tv.danmaku.ijk.media.** {*;}
#-keep enum tv.danmaku.ijk.media.** {*;}
#fastjson
-dontwarn com.alibaba.fastjson.**
-keepattributes Singature
-keepattributes *Annotation
#pldroid-player
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
-dontwarn com.pili.pldroid.player.**

#timber
-dontwarn org.jetbrains.annotations.**

#tape
-keep class com.squareup.tape2.** {*;}
-keep interface com.squareup.tape2.** {*;}
-keep enum com.squareup.tape2.** {*;}

#bugly and upgrade
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

#bd speech
-keep class com.baidu.speech.**{*;}

#zego
-keep class com.zego.**{*;}

#socket.io
-keep class io.socket.** {*;}
-keep interface io.socket.** {*;}
-keep enum io.socket.** {*;}

#aliyun pusher
-keep class com.alibaba.livecloud.** { *;}
-keep class com.alivc.** { *;}

#App
-keep class com.ncxx.flysee.thirdparty.http.bean.** {*;}
-keep interface com.ncxx.flysee.thirdparty.http.bean.** {*;}
-keep enum com.ncxx.flysee.thirdparty.http.bean.** {*;}

-keep class com.ncxx.flysee.bean.** {*;}
-keep interface com.ncxx.flysee.bean.** {*;}
-keep enum com.ncxx.flysee.bean.** {*;}

-keep class com.ncxx.flysee.data.** {*;}
-keep interface com.ncxx.flysee.data.** {*;}
-keep enum com.ncxx.flysee.data.** {*;}