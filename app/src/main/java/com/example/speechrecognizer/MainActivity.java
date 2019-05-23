package com.example.speechrecognizer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editText1,editText2,editText3;
    Button btnSpeak1,btnSpeak2,btnSpeak3,btnSend;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    int lastPressed;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        checkPermission();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference();


        editText1 = findViewById(R.id.editText1);
        btnSpeak1 = findViewById(R.id.buttonSpeak1);
        editText2 = findViewById(R.id.editText2);
        btnSpeak2 = findViewById(R.id.buttonSpeak2);
        editText3 = findViewById(R.id.editText3);
        btnSpeak3 = findViewById(R.id.buttonSpeak3);
        btnSend = findViewById(R.id.buttonSend);

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null)
                    switch (lastPressed) {
                        case 1:
                            editText1.setText(matches.get(0));
                            break;
                        case 2:
                            editText2.setText(matches.get(0));
                            break;
                        case 3:
                            editText3.setText(matches.get(0));
                            break;
                        default:
                            break;

                    }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        btnSpeak1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lastPressed=1;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText1.setHint("Insert the value");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        editText1.setText("");
                        editText1.setHint("Listening...");
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }

                return false;
            }
        });

        btnSpeak2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lastPressed=2;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText2.setHint("Insert the name");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        editText2.setText("");
                        editText2.setHint("Listening...");
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }

                return false;
            }
        });

        btnSpeak3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lastPressed=3;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText3.setHint("Insert the date");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        editText3.setText("");
                        editText3.setHint("Listening...");
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }

                return false;
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

        mFirebaseDatabase.child("nume1").setValue("valoareMaree2e");

            }
        });

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:"+getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}

