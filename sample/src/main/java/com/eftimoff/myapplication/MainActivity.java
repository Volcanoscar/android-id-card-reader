package com.eftimoff.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eftimoff.idcardreader.models.PassportType;
import com.eftimoff.idcardreader.ui.choose.OcrActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = OcrActivity.buildIntent().skipChooseStep(PassportType.BULGARIAN_ID_CARD_OLD).build(getApplicationContext());
        startActivity(intent);
    }
}
