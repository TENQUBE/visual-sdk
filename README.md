Visual
=====
- 소비내역 분석 및 관리
- 커머스 내역 분석 및 관리
- 실시간 내역 영수증 관리


Download
--------
[설치 및 사용법](https://github.com/TENQUBE/visual-sdk/wiki/%EC%84%A4%EC%B9%98%EB%B0%A9%EB%B2%95-%EB%B0%8F-%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4-%EC%84%A4%EB%AA%85)

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
    implementation 'com.tenqube.visual_third:app:0.1.2'
}
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
VisualService 요약
-------------------
```

interface VisualService {

    /**
     * 노티를 파싱합니다.
     */
    fun parseNoti(sbn: StatusBarNotification, isRcs: Boolean, listener: SmsListener)

    /**
     * sms or mms 를 파싱합니다.
     */
    fun parseSms(intent: Intent, listener: SmsListener)

    /**
     * rcs를 파싱 합니다.
     */
    fun parseRcs(uri: Uri, listener: SmsListener)

    /**
     * 사용자 가입
     * @param uid 고유 아이디
     */
    @Throws(ParameterException::class)
    fun signUp(uid: String, gender: Gender, birth: Int, onResultListener: OnResultListener)

    /**
     * @param activity Activity activity.startActivityForResult(intent, VISUAL_REQUEST_CODE);
     * 앱 finish 전달해줌 / acitivity 가 null 인경우 startActivity
     *
     * @param uid 사용자 아이디
     * @param path 팝업 선택시 deep link로 전달받은 path값
     * @throws ParameterException 파라미터가 올바르지 않은경우 예외 발생
     */
    @Throws(ParameterException::class)
    fun startVisual(activity: Activity?,
                    uid: String,
                    path: String): Unit

    fun startVisualDetail(activity: AppCompatActivity, uid: String, parseResult: ParseResult)

    fun getVisualFragment(activity: AppCompatActivity, uid: String): VisualMainFragment

    fun startTerms(activity: AppCompatActivity, uid: String, listener: TermsListener)

    /**
     * 사용자 초기화 함수
     * @param callback
     */
    fun signOut(callback: Callback<Boolean>)

    /**
     * 팝업 보여질지 여부를 확인 합니다.
     * @return 팝업 보여질 여부 (true : 보여짐 , false : 안보여짐)
     */
    fun isActiveTranPopup(): Boolean

    /**
     * 팝업 호출 여부
     * @param isActive 팝업 보여질 여부 (true : 보여짐 , false : 안보여짐)
     */
    fun setTranPopup(isActive: Boolean)

    /**
     * 알람 설정
     * @param type  ReportAlarmType.Daily, ReportAlarmType.Weekly, ReportAlarmType.Monthly 설정하고자 하는 타입
     * @param isActive 활성화 여부
     */
    fun setReportAlarm(type: ReportAlarmType, isActive: Boolean)

    /**
     *
     * @param type ReportAlarmType.Daily, ReportAlarmType.Weekly, ReportAlarmType.Monthly 설정하고자 하는 타입
     * @return 리포트 알람 활성화 여부
     */
    fun isActiveReportAlarm(type: ReportAlarmType): Boolean


    /**
     * 앱 알림 설정 여부 저장
     * @param isActive 활성화 여부
     */
    fun setAppNoti(isActive: Boolean)

    /**
     * @return 앱알림 활성화 여부
     */
    fun isActiveAppNoti(): Boolean

    /**
     * 로그 확인을 위한 함수
     * @param isActive 로그 확인 여부
     */
    fun setLogger(isActive: Boolean)

    /**
     * SDK초기화 함수 개발 전용
     */
    fun initSDK()

    /**
     * SDK 사용 여부 판단 플레그
     * @param enabled true : 동작 / false : 동작 안함.
     */
    fun setEnabled(enabled: Boolean)

    /**
     * 가입 여부
     * @return 가입여부 결과 리턴
     */
    fun isJoined(): Boolean
}

interface TermsListener {
    fun onAgreed()
}

interface SmsListener {
    fun onResult(result: ParseResult)
}

interface OnResultListener {
    fun onResult(signUpResult: SignUpResponse?, msg: String?)
}
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
```

```

//비주얼 시작하기
VisualManager.getInstance(this).startVisual(this);
        
```


Author
------
TENQUBE

License
-------
BSD, part MIT and Apache 2.0.


