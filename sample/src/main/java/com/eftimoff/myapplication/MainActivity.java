package com.eftimoff.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.eftimoff.idcardreader.models.IdCard;
import com.eftimoff.idcardreader.models.PassportType;
import com.eftimoff.idcardreader.ui.choose.OcrActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView middleName;
    private TextView lastName;
    private TextView idNumber;
    private TextView gender;
    private TextView personalNumber;
    private TextView dateOfBirth;
    private TextView expirationDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        if (savedInstanceState == null) {
            final Intent intent = OcrActivity.buildIntent()
                    .skipChooseStep(PassportType.BULGARIAN_ID_CARD_OLD)
                    .enableLogger(false)
                    .build(getApplicationContext());
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                final IdCard idCard = (IdCard) data.getSerializableExtra(OcrActivity.EXTRA_ID_CARD);
                bindIdCard(idCard);
            }
        }
    }

    private void bindIdCard(final IdCard idCard) {
        firstName.setText(idCard.getFirstName());
        middleName.setText(idCard.getMiddleName());
        lastName.setText(idCard.getLastName());
        idNumber.setText(idCard.getId());
        gender.setText(idCard.getGender().name());
        personalNumber.setText(idCard.getPersonalNumber());
        dateOfBirth.setText(new Date(idCard.getDateOfBirth() * 1000).toString());
        expirationDate.setText(new Date(idCard.getExpirationDate() * 1000).toString());
    }

    private void setupViews() {
        firstName = (TextView) findViewById(R.id.firstName);
        middleName = (TextView) findViewById(R.id.middleName);
        lastName = (TextView) findViewById(R.id.lastName);
        idNumber = (TextView) findViewById(R.id.idNumber);
        gender = (TextView) findViewById(R.id.gender);
        personalNumber = (TextView) findViewById(R.id.personalNumber);
        dateOfBirth = (TextView) findViewById(R.id.dateOfBirth);
        expirationDate = (TextView) findViewById(R.id.expirationDate);
    }
}
