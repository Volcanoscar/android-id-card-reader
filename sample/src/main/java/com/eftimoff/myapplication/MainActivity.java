package com.eftimoff.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eftimoff.idcardreader.models.IdCard;
import com.eftimoff.idcardreader.ui.choose.OcrActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = OcrActivity.buildIntent().build(getApplicationContext());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                final IdCard idCard = (IdCard) data.getSerializableExtra(OcrActivity.EXTRA_ID_CARD);
                Log.i("HELLO", idCard.getFirstName());
            }
        }
    }
}
