Visual
=====
- 소비내역 분석 및 관리
- 커머스 내역 분석 및 관리
- 실시간 내역 영수증 관리


Download
--------
use Gradle:


project.gradle
```
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

app.gradle
```
repositories {
  google()
  jcenter()
}

dependencies {
    implementation 'com.tenqube.visual_third:app:0.0.1'
}
```


AndroidMenifest
--------
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tenqube.visualsample">

    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.RECEIVE_SMS"/>
    <uses-permission android:name="android.permission.READ_SMS" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

    <application>
...
 

<!--        파일 저장-->
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths" />
        </provider>

<!--        알림 접근 허용-->
        <service
            android:name=".catcher.NotiCatcher"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
            <intent-filter>
                <action android:name="android.service.notification.NotificationListenerService" />
            </intent-filter>
        </service>

<!--        sms 수신 리시버-->
        <receiver
            android:name=".catcher.SMSCatcher"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.provider.Telephony.SMS_RECEIVED" />
            </intent-filter>
        </receiver>

    </application>

</manifest>

```

ProGuard
--------

Depending on your ProGuard (DexGuard) config and usage, you may need to include the following lines in your proguard.cfg 

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


