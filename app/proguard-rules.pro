# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.tenqube.visual_third.** { *; }
-keep interface com.tenqube.visual_third.** { *; }
-dontwarn com.tenqube.visual_third.**
-dontnote com.tenqube.visual_third.**

-keep class tenqube.parser.** {*;}
-dontwarn tenqube.parser.**

-keep class com.tenqube.commerce.** {*;}
-dontwarn com.tenqube.commerce.**

-keep class com.tenqube.notiparser.** {*;}
-dontwarn com.tenqube.notiparser.**

-keep class org.koin.** {*;}
-dontwarn org.koin.**

-keep class org.jsoup.** {*;}
-dontwarn org.jsoup.**

-keep class android.webkit.** {*;}
-dontwarn android.webkit.**


-keepnames class android.arch.lifecycle.ViewModel
-keepclassmembers public class * extends android.arch.lifecycle.ViewModel { public <init>(...); }
-keepclassmembers class * { public <init>(...); }

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }

-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

-dontwarn kotlinx.coroutines.flow.**

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.corㅇoutines.**

-keep class com.ksp.mobilesdkjava.OScriptSystem.** { *;  }
-keep class com.ksp.mobilesdkjava.MarketSystem.** { *;  }
-keep class com.ksp.mobilesdkjava.BOESystem.** { *;  }
-keep class com.ksp.mobilesdkjava.OJsonSystem.** { *;  }
-keep class com.google.gson.** { *;  }
-keep class com.ksp.mobilesdkjava.OHttpSystem.** { *;  }