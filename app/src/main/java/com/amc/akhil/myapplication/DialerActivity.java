package com.amc.akhil.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class DialerActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener, View.OnLongClickListener{

    private static final int TTS_CHECK_CODE = 0x122;
    private static final int REQUEST_CALL = 0x4156;
    private EditText screen;
    private TextToSpeech mTts;
    private Button mDialButton;
    private boolean shouldShowPermissionAgain;
    private TTSServiceActivity ttsServiceActivity;

    private TTSServiceActivity mTtsServiceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialer);
        initializeView();
        initializeReference();
    }

    public void initializeReference() {
        checkForTTSPresent();
    }

    public void checkForTTSPresent() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS_CHECK_CODE);
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    private void initializeView() {
        screen = (EditText) findViewById(R.id.screen);
        screen.setCursorVisible(false);
        int buttonList[] = {R.id.callbutton, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.buttonStar, R.id.buttonHash, R.id.buttonZero, R.id.buttonDel};
        for (int d : buttonList) {
            View v = findViewById(d);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }
    }

    public void display(String value) {
        screen.append(value);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TTS_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(DialerActivity.this, this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callbutton:
                if (screen.getText().toString().length() > 0) {
                    //speakOut("Calling Number is " + screen.getText().toString());
                    speakOut("Please press long press for make a call");
                } else {
                    speakOut("Please input some number to call");
                }
                break;
            case R.id.button2:
                speakOut("1");
                break;
            case R.id.button3:
                speakOut("2");
                break;
            case R.id.button4:
                speakOut("3");
                break;
            case R.id.button5:
                speakOut("4");
                break;
            case R.id.button6:
                speakOut("5");
                break;
            case R.id.button7:
                speakOut("6");
                break;
            case R.id.button8:
                speakOut("7");
                break;
            case R.id.button9:
                speakOut("8");
                break;
            case R.id.button10:
                speakOut("9");
                break;
            case R.id.buttonStar:
                speakOut("*");
                break;
            case R.id.buttonHash:
                speakOut("#");
                break;
            case R.id.buttonZero:
                speakOut("0");
                break;
            case R.id.buttonDel:
                speakOut("Delete");
                screen.getText().delete(screen.getText().length()-1, screen.getText().length());
                break;
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

                        ActivityCompat.requestPermissions(DialerActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
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

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.callbutton:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkCallPermission(screen.getText().toString());
                } else {
                    call(screen.getText().toString());
                }
                break;
            case R.id.button2:
                display("1");
                speakOut("1");
                break;
            case R.id.button3:
                display("2");
                speakOut("2");
                break;
            case R.id.button4:
                display("3");
                speakOut("3");
                break;
            case R.id.button5:
                display("4");
                speakOut("4");
                break;
            case R.id.button6:
                display("5");
                speakOut("5");
                break;
            case R.id.button7:
                display("6");
                speakOut("6");
                break;
            case R.id.button8:
                display("7");
                speakOut("7");
                break;
            case R.id.button9:
                display("8");
                speakOut("8");
                break;
            case R.id.button10:
                display("9");
                speakOut("9");
                break;
            case R.id.buttonZero:
                display("0");
                break;
            case R.id.buttonDel:
                if (screen.getText().toString().length() > 0) {
                    String newScreen = screen.getText().toString().substring(0, screen.getText().toString().length() - 1);
                    speakOut("You delete " + screen.getText().toString().substring(screen.getText().toString().length() - 1));
                    screen.setText(newScreen);
                    if (screen.getText().toString().length() > 0){
                        //speakOut("Current Phone number is " + screen.getText().toString());
                    }
                } else {
                    speakOut("No number to Delete");
                }
                break;
        }
        return true;
    }

    public void call(String phoneNumber) {
        if (phoneNumber.length() > 0) {
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

    public void goToSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    public TTSServiceActivity getTtsServiceActivity() {
        return mTtsServiceActivity;
    }

    private void speakOut(String tts_text) {
        mTts.speak(tts_text, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, EmergencyActivity.class));
    }
}
