package com.tenqube.visualsample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class VisualSampleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_test);

        findViewById(R.id.web).setOnClickListener(v -> startLgPay());
    }

    private void startLgPay() {
        startActivity(new Intent(this, LGPayActivity.class));
    }



}