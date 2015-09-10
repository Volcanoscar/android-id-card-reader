package com.eftimoff.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.eftimoff.idcardreader.models.IdCard;
import com.eftimoff.idcardreader.models.PassportType;
import com.eftimoff.idcardreader.ui.choose.OcrActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private TextView firstName;
    private TextView middleName;
    private TextView lastName;
    private TextView idNumber;
    private TextView gender;
    private TextView personalNumber;
    private TextView dateOfBirth;
    private TextView expirationDate;
    private TextView address;
    private TextView idCreationDate;
    private TextView placeOfBirth;
    private TextView height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        if (savedInstanceState == null) {
            final Intent intent = OcrActivity.buildIntent()
                    .skipChooseStep(PassportType.SENEGAL_ID_CARD)
                    .enableLogger(true)
                    .showTempResults(true)
                    .percentageToCapture(75)
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
        dateOfBirth.setText(dateFormat.format(new Date(idCard.getDateOfBirth() * 1000)));
        expirationDate.setText(dateFormat.format(new Date(idCard.getExpirationDate() * 1000)));
        address.setText(idCard.getAddress());
        idCreationDate.setText(dateFormat.format(new Date(idCard.getIdCreatedDate() * 1000)));
        placeOfBirth.setText(idCard.getPlaceOfBirth());
        height.setText("" + idCard.getHeight());
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
        address = (TextView) findViewById(R.id.address);
        idCreationDate = (TextView) findViewById(R.id.idCreationDate);
        placeOfBirth = (TextView) findViewById(R.id.placeOfBirth);
        height = (TextView) findViewById(R.id.height);
    }
}
