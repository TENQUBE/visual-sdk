package com.tenqube.visualsample.visual;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.service.notification.StatusBarNotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tenqube.visual_third.SmsListener;
import com.tenqube.visual_third.TermsListener;
import com.tenqube.visual_third.VisualBuilder;
import com.tenqube.visual_third.VisualService;
import com.tenqube.visual_third.domain.exception.ParameterException;
import com.tenqube.visual_third.presentation.ui.main.VisualMainFragment;
import com.tenqube.visual_third.presentation.util.Callback;
import com.tenqube.visual_third.presentation.util.Constants;
import com.tenqube.visual_third.presentation.util.VisualCallback;
import com.tenqube.visual_third.thirdparty.analysis.model.Analysis;
import com.tenqube.visual_third.usecase.parsing.dto.ParseResult;
import com.tenqube.visual_third.usecase.parsing.dto.ParseStatus;
import com.tenqube.visualsample.R;

import java.util.ArrayList;

import tenqube.parser.BuildConfig;

/**
 * 전달받은 api 키정보를 입력해주세
 */
enum VisualLayer {
    Dev("CFMmLz1lI41EmEqnyxwoagamUupWz4D9XoGF3kaj", Constants.DEV),
    Prod("r8scLJTRdd8NgE1EEVkaU1hoyQDRr6G76kIskuyr", Constants.PROD);

    String apiKey;
    String layer;
    VisualLayer(String apiKey, String layer) {
        this.apiKey = apiKey;
        this.layer = layer;
    }
}

public class VisualManager {

    private static VisualManager mInstance;


    private VisualService visualService;// 비주얼 서비스 객체
    private String uid = "uid";
    private Context context;

    public static VisualManager getInstance(Context context){

        synchronized (VisualManager.class) {
            if(mInstance == null){
                mInstance = new VisualManager(context);
            }
        }
        return mInstance;
    }

    private VisualManager(Context context){
        this.context = context;
    }

    public void initialize() {
        try {
            visualService = new VisualBuilder()
                    .withContext(context) // context 필수로 주입 해주세요
                    .apiKey((VisualLayer.Dev.apiKey)) // DEV_API_KEY,// 전달받은 api key devKey, prodKey
                    .qualifier(VisualLayer.Dev.layer) // Constants.DEV,// or Constants.PROD
                    .authority(BuildConfig.APPLICATION_ID + ".provider") // // AndroidMenifest.xml fileprovider  android:authorities="${applicationId}.provider"
                    .notification(R.drawable.visual_ic_deposit_etc, ":channel_name", 0) // 보고서 알림 사용을 위해 필요합니다.
                    .logger(false) // 로그 확인 플래그
                    .visualCallback(new VisualCallback() { // 가계부 내부 동작에 대한 콜백을 받아옵니다.
                        @Override
                        public void onPopupChange(boolean isActive) { // 팝업 온오프 시

                        }

                        @Override
                        public void onReportChange(Constants.ReportAlarmType type, boolean isActive) { // 보고서 알림 온오프시

                        }

                        @Override
                        public void onSignOut() { // 서비스 철회시

                        }
                    }) // 내역 수신 확인
                    .build();
        } catch (ParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 비주얼 시작시 사용합니다.
     *
     * @param activity
     */
    public void startVisual(AppCompatActivity activity) {
        if(visualService == null) {
            initialize();
        }

        isAgreed(value -> {
                if(value) {
                    try {
                        visualService.startVisual(activity, uid, "");
                    } catch (ParameterException e) {
                        e.printStackTrace();
                    }
                } else {
                    startTerms(activity, () -> {
                        startVisual(activity);
                    });
                }

        });
    }

    public void startTerms(AppCompatActivity activity, TermsListener listener) {

        visualService.startTerms(activity, uid, () -> {
            // 서버 약관동의승인됨 저장
            saveOnAgreed();
            listener.onAgreed();
        });
    }

    /**
     * 후후 약관동의 여부를 조회합니다.
     *
     * @return 약관 동의 여부
     */
    private void isAgreed(Callback<Boolean> callback) {

        callback.onDataLoaded(visualService.isJoined());
    }

    /**
     * 서버에 약관동의 승인됨을 저장합니다.
     */
    private void saveOnAgreed() {

    }

    /**
     * 프레그 먼트 commit시에 사용됩니다.
     * @param activity
     */
    public void startReport(AppCompatActivity activity) {

        if(visualService == null) {
            initialize();
        }
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.weekly_container, getVisualFragment(activity))
                .commitAllowingStateLoss();
    }


    /**
     * fragment 조회시 사용됩니다 기존 pager에서 사용한 fragment 조회시 사용합니다.
     * 약관동의가 되어있지 않은 경우 약관동의후
     *
     * @param activity
     * @return
     */
    public Fragment getVisualFragment(AppCompatActivity activity) {
        if(visualService == null) {
            initialize();
        }

        VisualMainFragment fragment =  visualService.getVisualFragment(activity, uid);

        // 약관 동의후에 fragment 를 다시 그려주기 위해 fragment.start() 를 호출합니다.
        isAgreed(value -> {
            if(!value) {
                visualService.startTerms(activity, uid, fragment::start);
            }
        });
        return fragment;
    }

    /**
     * 영수증 화면에서 가계부 상세보기 클릭시 사용합니다.
     * @param activity
     */
    public void startVisualDetail(AppCompatActivity activity) {
        if(visualService == null) {
            initialize();
        }
        visualService.startVisualDetail(activity, uid, new ParseResult(ParseStatus.Parsed, null, null, new Analysis(1, "lv0_mid_food_dining_place", "식사", "지난달 식사 지출은\\n32.3만원입니다.", "주요 지출처는\n(주)우아한형제들 (17만원)입니다.", new ArrayList<>(), 1)));
    }

    /**
     * 단건 파싱시 사용합니다.
     * @param intent
     * @param smsListener
     */
    public void parseSms(Intent intent, SmsListener smsListener) {
        visualService.parseSms(intent, smsListener);
    }

    /**
     * Rcs 파싱시 사용합니다.
     * @param uri
     * @param listener
     */
    public void parseRcs(Uri uri, SmsListener listener) {
        visualService.parseRcs(uri, listener);
    }

    /**
     * 알림 파싱시 사용합니다.
     * @param sbn
     * @param listener
     */
    public void parseNoti(StatusBarNotification sbn, SmsListener listener) {
        visualService.parseNoti(sbn, listener);
    }
}