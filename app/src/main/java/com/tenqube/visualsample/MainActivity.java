package com.tenqube.visualsample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.tenqube.visualsample.visual.VisualManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        findViewById(R.id.web).setOnClickListener(v -> startVisual());

        findViewById(R.id.weekly_report).setOnClickListener(view -> startReport());


        findViewById(R.id.receipts).setOnClickListener(view -> startVisualDetail());

        VisualManager.getInstance(this).parseRcs(); // RCS 파싱합니다.

    }

    /**
     * 권한은 필수로 필요하며 비주얼 화면을 시작합니다.
     */
    private void startVisual() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS
            )) {

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                        0);

            }
        } else {
            VisualManager.getInstance(this).startVisual(this);
        }

    }

    /**
     *
     */
    private void startReport() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS
            )) {

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                        0);

            }
        } else {
            VisualManager.getInstance(this).startReport(this);
        }
    }

    /**
     * 비주얼 상세화면 시작하기
     */
    private void startVisualDetail() {
        VisualManager.getInstance(this).startVisualDetail(this);
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

}