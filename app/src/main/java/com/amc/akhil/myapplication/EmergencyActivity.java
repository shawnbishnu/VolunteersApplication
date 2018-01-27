package com.amc.akhil.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class EmergencyActivity extends AppCompatActivity implements View.OnClickListener,TextToSpeech.OnInitListener{

    private static final int REQUEST_CALL = 0x4156;
    private static final int TTS_CHECK_CODE = 0x122;
    private EditText screen;
    private boolean shouldShowPermissionAgain;
    private TextToSpeech mTts;
    private Button mDialButton;
    private CardView mCardView1;
    private CardView mCardView2;
    private CardView mCardView3;
    private CardView mCardView4;
    private CardView mCardView5;
    private CardView mCardView6;

    private TTSServiceActivity ttsServiceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        initializeReferences();
        checkForTTSPresent();
    }

    @Override
    public void onClick(View view) {
        TextView phoneNumberView;
        String phoneNumber;
        if(view == mDialButton){
            startActivity(new Intent(this, DialerActivity.class));
        }
        if(view == mCardView1){
            phoneNumberView = (TextView) findViewById(R.id.item_text1);
            phoneNumber = (String) phoneNumberView.getText();
            call(phoneNumber);
        }
        if(view == mCardView2){
            phoneNumberView = (TextView) findViewById(R.id.item_text2);
            phoneNumber = (String) phoneNumberView.getText();
            call(phoneNumber);
        }
        if(view == mCardView3){
            phoneNumberView = (TextView) findViewById(R.id.item_text3);
            phoneNumber = (String) phoneNumberView.getText();
            call(phoneNumber);
        }
        if(view == mCardView4){
            phoneNumberView = (TextView) findViewById(R.id.item_text4);
            phoneNumber = (String) phoneNumberView.getText();
            call(phoneNumber);
        }
        if(view == mCardView5){
            phoneNumberView = (TextView) findViewById(R.id.item_text5);
            phoneNumber = (String) phoneNumberView.getText();
            call(phoneNumber);
        }
        if(view == mCardView6){
            phoneNumberView = (TextView) findViewById(R.id.item_text6);
            phoneNumber = (String) phoneNumberView.getText();
            call(phoneNumber);
        }
    }

    public void call(String phoneNumber) {
        if (phoneNumber.length() > 0) {
            speakOut("Calling phone number ");
            speakOut(phoneNumber);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Error Occur!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please Enter a Number", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call(screen.getText().toString());
            } else {
                if (!shouldShowPermissionAgain && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    goToSettings();
                    Toast.makeText(this, "Please give Call Permission to perform this action", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void checkCallPermission(String phoneNumber) {
        shouldShowPermissionAgain = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

                AlertDialog.Builder phone_explanation = new AlertDialog.Builder(this);
                phone_explanation.setMessage("For calling we need to call permission.Do you want to allow call ?");
                phone_explanation.setTitle("Need Call Permission");
                phone_explanation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                        shouldShowPermissionAgain = true;
                    }
                });
                phone_explanation.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                phone_explanation.show();


            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }
        } else {
            call(phoneNumber);
        }
    }

    private void checkForTTSPresent() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS_CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TTS_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(EmergencyActivity.this, this);
                mTts.setLanguage(Locale.US);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    private void speakOut(String tts_text) {
        try {
            if(isNumber(tts_text)){
                for(int i =0; i <tts_text.length(); i++){
                    mTts.speak(String.valueOf(tts_text.charAt(i)), TextToSpeech.QUEUE_ADD, null);
                }
                Thread.sleep(6000);
            } else{
                mTts.speak(tts_text, TextToSpeech.QUEUE_ADD, null);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    public void goToSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void initializeReferences() {
        mDialButton = (Button) findViewById(R.id.dialButton);
        mCardView1 = (CardView) findViewById(R.id.cardView1);
        mCardView2 = (CardView) findViewById(R.id.cardView2);
        mCardView3 = (CardView) findViewById(R.id.cardView3);
        mCardView4 = (CardView) findViewById(R.id.cardView4);
        mCardView5 = (CardView) findViewById(R.id.cardView5);
        mCardView6 = (CardView) findViewById(R.id.cardView6);
        mDialButton.setOnClickListener(this);
        mCardView1.setOnClickListener(this);
        mCardView2.setOnClickListener(this);
        mCardView3.setOnClickListener(this);
        mCardView4.setOnClickListener(this);
        mCardView5.setOnClickListener(this);
        mCardView6.setOnClickListener(this);
        checkForTTSPresent();
    }


    @Override
    public void onInit(int status) {

    }

    private boolean isNumber(String word)
    {
        boolean isNumber = false;
        try
        {
            int number = NumberFormat.getInstance().parse(word).intValue();
            isNumber = true;
        } catch (ParseException e) {
           isNumber = false;
        }
        return isNumber;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
