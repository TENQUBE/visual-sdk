Visual
=====
소비내역 분석 및 관리
커머스 내역 분석 및 관리
실시간 내역 영수증 관리


Download
--------
use Gradle:



```project.gradle
builscript {
...
}
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://dl.bintray.com/tenqube/visual/"
        }
    }
}
```

```app.gradle
repositories {
  google()
  jcenter()
}

dependencies {
    implementation 'com.tenqube.visual_third:app:0.0.1'
}
```


ProGuard
--------

Depending on your ProGuard (DexGuard) config and usage, you may need to include the following lines in your proguard.cfg (see the [Download and Setup docs page][25] for more details):

```pro
-keep class com.tenqube.visual_third.** { *; }
-keep interface com.tenqube.visual_third.** { *; }
-dontwarn com.tenqube.visual_third.**
-dontnote com.tenqube.visual_third.**

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }

-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

-dontwarn kotlinx.coroutines.flow.**

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**
```

How do I use Visual?
-------------------

Simple use cases will look something like this:

```java
public class VisualSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VisualManager.getInstance(this).initialize();

        RcsCatcher.getInstance(this).register(); //rcs 등록
    }
}

//비주얼 시작하기
VisualManager.getInstance(this).startVisual(this);
        
```


Author
------
TEHQNBUE

License
-------
BSD, part MIT and Apache 2.0.


