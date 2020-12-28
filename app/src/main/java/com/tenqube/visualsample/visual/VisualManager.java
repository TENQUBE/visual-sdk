package com.tenqube.visualsample.visual;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.service.notification.StatusBarNotification;

import androidx.appcompat.app.AppCompatActivity;

import com.tenqube.visual_third.SmsListener;
import com.tenqube.visual_third.VisualBuilder;
import com.tenqube.visual_third.VisualService;
import com.tenqube.visual_third.domain.exception.ParameterException;
import com.tenqube.visual_third.presentation.util.Constants;
import com.tenqube.visual_third.presentation.util.VisualCallback;
import com.tenqube.visual_third.thirdparty.analysis.model.Analysis;
import com.tenqube.visual_third.usecase.parsing.dto.ParseResult;
import com.tenqube.visual_third.usecase.parsing.dto.ParseStatus;
import com.tenqube.visualsample.R;

import java.util.ArrayList;

import tenqube.parser.BuildConfig;

public class VisualManager {

    private static VisualManager mInstance;
    public static final String API_KEY = "CFMmLz1lI41EmEqnyxwoagamUupWz4D9XoGF3kaj"; //api 키정보
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
                    .apiKey((API_KEY)) // DEV_API_KEY,// 전달받은 api key devKey, prodKey
                    .qualifier(Constants.DEV) // Constants.DEV,// or Constants.PROD
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

    public void startVisual(AppCompatActivity activity) {
        if(visualService == null) {
            initialize();
        }
        if(visualService.isJoined()) {
            try {
                visualService.startVisual(activity, uid, "");
            } catch (ParameterException e) {
                e.printStackTrace();
            }
        } else {
//            visualService.startTerms(activity, uid, () -> {
//                startVisual(activity);
//
//            });
        }
    }

    public void startReport(AppCompatActivity activity) {

        try {
            if(visualService == null) {
                initialize();
            }
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weekly_container, visualService.getVisualFragment(activity, uid, () -> {
                        // 서버저장
                    }))
                    .commitAllowingStateLoss();
        } catch (ParameterException e) {
            e.printStackTrace();
        }
    }

    public void startCommerce(AppCompatActivity activity) {
        if(visualService == null) {
            initialize();
        }
        if(visualService.isJoined()) {
            try {
                visualService.startVisual(activity, uid, "commerce");
            } catch (ParameterException e) {
                e.printStackTrace();
            }
        } else {
            visualService.startTerms(activity, uid, () -> {

            });
        }
    }

    public void startVisualDetail(AppCompatActivity activity) {
        if(visualService == null) {
            initialize();
        }
        visualService.startVisualDetail(activity, uid, new ParseResult(ParseStatus.Parsed, null, null, new Analysis(1, "lv0_mid_food_dining_place", "식사", "지난달 식사 지출은\\n32.3만원입니다.", "주요 지출처는\n(주)우아한형제들 (17만원)입니다.", new ArrayList<>(), 1)));
    }

    public void parseSms(Intent intent, SmsListener smsListener) {
        visualService.parseSms(intent, smsListener);
    }

    public void parseRcs(Uri uri, SmsListener listener) {
        parseRcs(uri, listener);
    }

    public void parseNoti(StatusBarNotification sbn, SmsListener listener) {
        visualService.parseNoti(sbn, listener);
    }
}