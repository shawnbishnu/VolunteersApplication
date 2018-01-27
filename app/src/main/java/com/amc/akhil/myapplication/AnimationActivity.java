package com.amc.akhil.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AnimationActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech mTts;
    private static final int TTS_CHECK_CODE = 0x122;
    private TTSServiceActivity ttsServiceActivity;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        initializeReferences();
        animationView.setAnimation("scan_animation.json");
        animationView.loop(true);
        animationView.playAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setContentView(R.layout.request_acknowledgement);
                startActivity(new Intent(getApplicationContext(), RequestAcknowledgementActivity.class));
            }
        },4000);
        //speakOut("Looking for volunteers ...please wait");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TTS_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(AnimationActivity.this, this);
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

    private void initializeReferences() {
        checkForTTSPresent();
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
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

    public void speakOut(String tts_text) {
        try {
            mTts.speak(tts_text, TextToSpeech.QUEUE_ADD, null);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Exception while TTS "+e.getMessage());
        }
    }

    public TTSServiceActivity getTtsServiceActivity() {
        return ttsServiceActivity;
    }

    @Override
    public void onInit(int status) {

    }
}
