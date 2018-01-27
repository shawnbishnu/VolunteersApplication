package com.amc.akhil.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class RequestAcknowledgementActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextView acknowledgementTextView;
    private TextToSpeech mTts;
    private static final int TTS_CHECK_CODE = 0x122;
    private TTSServiceActivity ttsServiceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_acknowledgement);
        initializeReferences();
        //speakOut(acknowledgementTextView.getText().toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        },4000);
    }

    private void initializeReferences() {
        checkForTTSPresent();
        acknowledgementTextView = (TextView) findViewById(R.id.acknowledgement);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TTS_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(RequestAcknowledgementActivity.this, this);
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

    public void speakOut(String tts_text) {
        try {
            mTts.speak(tts_text, TextToSpeech.QUEUE_ADD, null);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Exception while TTS "+e.getMessage());
        }
    }

    @Override
    public void onInit(int status) {

    }

    public TTSServiceActivity getTtsServiceActivity() {
        return ttsServiceActivity;
    }

}
