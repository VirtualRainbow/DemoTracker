# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\James\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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
-keep class com.jakewharton.timber.** { *; }

#design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#appcompat
-dontnote android.support.**
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

#samsung
-keep class !android.support.v7.internal.view.menu.MenuBuilder
-keep class !android.support.v7.internal.view.menu.SubMenuBuilder

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

#cardview
-keep class android.support.v7.widget.RoundRectDrawable { *; }

#rx
-dontwarn sun.misc.Unsafe
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}

#retrolambda
-dontwarn java.lang.invoke.*

-keepattributes Signature

-dontwarn com.squareup.okhttp.**

-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontnote com.afollestad.materialdialogs.internal.**
-dontnote com.google.android.gms.common.api.internal.**
-dontnote rx.internal.util.**