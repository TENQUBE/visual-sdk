package com.tenqube.visualsample;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tenqube.visual_third.Callback;
import com.tenqube.visual_third.Constants;
import com.tenqube.visual_third.SyncListener;
import com.tenqube.visual_third.SyncService;
import com.tenqube.visual_third.VisualCallback;
import com.tenqube.visual_third.VisualService;
import com.tenqube.visual_third.VisualServiceImpl;
import com.tenqube.visual_third.VisualSummary;
import com.tenqube.visual_third.VisualUserInfo;
import com.tenqube.visual_third.exception.ParameterException;
import com.tenqube.visual_third.model.parser.SyncData;
import com.tenqube.visual_third.model.parser.SyncTransaction;
import com.tenqube.visual_third.ui.OnResultListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.tenqube.visual_third.Constants.ACTION_TRAN_POPUP;
import static com.tenqube.visual_third.ui.VisualWebActivity.VISUAL_REQUEST_CODE;


public class LGPayActivity extends AppCompatActivity implements SyncService, VisualCallback {

    public static final String DEV_API_KEY = "GiAZLGhzbD2tIekv3uMHU5pf3tVR24z34ft1cBgy"; //api 키정보
    public static final String PROD_API_KEY = "1v9Vq5nKnU6kpqTSFqb3W97usJEIM7s88PkuX8W9"; //api 키정보

    private VisualService visualService;// 비주얼 서비스 객체
    private String visualPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lg);

        try {
            // visual service 생성
            visualService = new VisualServiceImpl.Builder()
                    .withContext(this)
                    .apiKey((DEV_API_KEY)) // DEV_API_KEY,// 전달받은 api key devKey, prodKey
                    .qualifier(Constants.DEV) // Constants.DEV,// or Constants.PROD
                    .authority(BuildConfig.APPLICATION_ID + ".provider") // // AndroidMenifest.xml fileprovider  android:authorities="${applicationId}.provider"
                    .notification(R.drawable.visual_ic_deposit_etc, "visual", 0)
                    .logger(true) // 로그 확인 플래그
                    .visualCallback(this) // 내역 수신 확인
                    .build();

            // daily 알림 설정
            Switch daily = findViewById(R.id.daily);
            daily.setChecked(visualService.isActiveReportAlarm(Constants.ReportAlarmType.DAILY));
            daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    visualService.setReportAlarm(Constants.ReportAlarmType.DAILY, isChecked);
                }
            });

            // weekly 알림 설정
            Switch weekly = findViewById(R.id.weekly);
            weekly.setChecked(visualService.isActiveReportAlarm(Constants.ReportAlarmType.WEEKLY));

            weekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    visualService.setReportAlarm(Constants.ReportAlarmType.WEEKLY, isChecked);
                }
            });

            // monthly 알림설정
            Switch monthly = findViewById(R.id.monthly);
            monthly.setChecked(visualService.isActiveReportAlarm(Constants.ReportAlarmType.MONTHLY));
            monthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    visualService.setReportAlarm(Constants.ReportAlarmType.MONTHLY, isChecked);
                }
            });

            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_TRAN_POPUP);

            findViewById(R.id.web).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startVisual();
                }
            });

            findViewById(R.id.report_test).setOnClickListener(v -> {


                getVisualSummary();

            });

            findViewById(R.id.rcs_test).setOnClickListener(v -> {

                visualService.parseRcs(value -> {

                    // reload 로직 추가합니다.

                });

            });

            createNotificationChannel();
        } catch (ParameterException e) {
            e.printStackTrace();
        }

    }

    private void getSum() {
        visualService.getSum(0, Calendar.getInstance().getTimeInMillis(), new Callback<VisualSummary>() {
            @Override
            public void onDataLoaded(final VisualSummary value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "value:" + value.getMonthSum() , Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    /**
     * 비주얼 월별 요약정보를 가져옵니다.
     */
    private void getVisualSummary() {
        visualService.getVisualSummary(value -> {

            String des =  value.getDescription();
            Toast.makeText(getApplicationContext(), "sum" + value.getMonthSum() + "value:" + des, Toast.LENGTH_LONG).show();

        });
    }


    private void startVisual() {

        if (ContextCompat.checkSelfPermission(LGPayActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LGPayActivity.this,
                    Manifest.permission.READ_SMS
            )) {

            } else {

                ActivityCompat.requestPermissions(LGPayActivity.this,
                        new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                        0);

            }
        } else {
            try {
                // LGPayActivity.this 값을 통해 startActivityForResult 로 호출합니다.
                // user 고유 아이디 정보를 추가해 주세요.
                visualService.startVisual(LGPayActivity.this, // activity 정보
                        ":serviceId", // 사용자 uid
                        new VisualUserInfo(Constants.Gender.MALE, 1987), // 사용자 정보
                        this,// SyncService 데이터 동기화를 위해 필요 처음 사용자인경우 null 허용됩니다.
                        new OnResultListener() {
                    @Override
                    public void onResult(int signUpResult, String msg) {

                        Toast.makeText(getApplicationContext(), "result:" + signUpResult + "msg" + msg, Toast.LENGTH_LONG).show();

                    }
                });
            } catch (ParameterException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVisual();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case VISUAL_REQUEST_CODE:
                // 화면 종료시 해당 code로 콜백
                // activity 주입한경우 콜백됩니다.
                break;
        }
    }


    /**
     *
     * 서버에서 사용자 sync정보를 받아옵니다.
     * 현재 목으로 처리되었습니다.
     * @return SyncData
     */
    @Override
    public void getSyncData(SyncListener syncListener) {

        try {
            onSuccess(syncListener);
        } catch (IOException e) {
            onError(syncListener);
        }
    }

    private void onSuccess(final SyncListener syncListener) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SyncTransaction> transactions = new ArrayList();
                for(int i = 0; i < 100 ; i++) {
                    SyncTransaction syncTransaction = new SyncTransaction();
                    syncTransaction.identifier = "1767469455598";

                    syncTransaction.cardName = "testCard";
                    syncTransaction.cardType = 0;
                    syncTransaction.cardSubType = 0;

                    syncTransaction.keyword = "홈플러스익스프레스";
                    syncTransaction.companyTitle = "외식";
                    syncTransaction.franchise = "none";
                    syncTransaction.currency = "KRW";
                    syncTransaction.dwType = 1;
                    syncTransaction.isCustom = 0;
                    syncTransaction.installmentCount = 1;
                    syncTransaction.oriSpentMoney = 9000.0;
                    syncTransaction.spentMoney = 9000.0;

                    syncTransaction.spentDate = "2018-10-15 22:28:16";
                    syncTransaction.finishDate = "2018-10-15 22:28:16";

                    syncTransaction.sender = "15888100";

                    syncTransaction.categoryCode = 321616;

                    transactions.add(syncTransaction);

                    syncTransaction = new SyncTransaction();
                    syncTransaction.identifier = "1" + System.currentTimeMillis() + "";

                    syncTransaction.cardName = "testCard2";
                    syncTransaction.cardType = 1;
                    syncTransaction.cardSubType = 0;

                    syncTransaction.keyword = "테스트2";
                    syncTransaction.companyTitle = "외식";
                    syncTransaction.franchise = "none";
                    syncTransaction.currency = "KRW";
                    syncTransaction.dwType = 1;
                    syncTransaction.isCustom = 0;
                    syncTransaction.installmentCount = 1;
                    syncTransaction.oriSpentMoney = 100;
                    syncTransaction.spentMoney = 100;

                    syncTransaction.spentDate = "2020-07-10 10:00:00";
                    syncTransaction.finishDate = "2020-07-10 10:00:00";

                    syncTransaction.sender = "16001111";


                    syncTransaction.categoryCode = 221010;

                    transactions.add(syncTransaction);
                }


                syncListener.onSuccess(new SyncData(transactions));

            }
        }).run();


    }

    private void onError(SyncListener syncListener) {
        syncListener.onError();
    }

    /**
     * 성공 실패 콜백
     * @param isSuccess sync 성공여부
     */
    @Override
    public void onResult(boolean isSuccess) {
        // 동기화 성공 실패 결과를 전달 합니다.

        Toast.makeText(getApplicationContext(), "동기화 성공 여부: " + isSuccess , Toast.LENGTH_LONG).show();
    }

    /**
     * 내역 수신시 수신되는 함수
     */
    @Override
    public void onTransactionReceived() {
        Toast.makeText(getApplicationContext(), "onTransactionReceived" , Toast.LENGTH_LONG).show();

    }

    /**
     * 서비스 철회시 호출되는 함수
     */
    @Override
    public void onSignOut() {

    }

    /**
     * 앱내에 채널이 미리 생성되어 있어야 합니다.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("visual",
                    "visual",
                    importance);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
        }
    }
}
