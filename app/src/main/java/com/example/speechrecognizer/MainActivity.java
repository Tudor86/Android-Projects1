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
import android.widget.Toast;

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
                            processText(matches.get(0),1);
                            editText1.setText(processText(matches.get(0),1));
                            break;
                        case 2:
                            processText(matches.get(0),1);
                            editText2.setText(processText(matches.get(0),2));
                            break;
                        case 3:
                            editText3.setText(processText(matches.get(0),3));
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
                        editText1.setHint("Value( ex. lei25.3 , or $13.26)");
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
                        editText2.setHint("Name");
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
                        editText3.setHint("Date(ex. 02.07.2019)");
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
                Bon bon=new Bon(editText1.getText().toString(),editText2.getText().toString(),editText3.getText().toString());

        mFirebaseDatabase.push().setValue(bon);
        //mFirebaseDatabase.setValue(bon);
                Toast.makeText(getApplicationContext(),"Sent to database!", Toast.LENGTH_SHORT).show();
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");

            }
        });

    }

    public String processText(String text,int numar){
        if(numar==1)
        {
            text=text.toLowerCase();
            text=text.trim();
            if(text.contains("l")) {
                text = text.replaceAll("[^\\d.]", "");
                text=text.trim();
                text="lei"+text;
            }

            return text;
        }
        if (numar==2)
            return text;

        if(numar==3)
        {
            text=text.toLowerCase();
            text=text.replace(" ",".");
            text=text.replace("january","01");
            text=text.replace("february","02");
            text=text.replace("march","03");
            text=text.replace("april","04");
            text=text.replace("may","05");
            text=text.replace("june","06");
            text=text.replace("july","07");
            text=text.replace("august","08");
            text=text.replace("september","09");
            text=text.replace("october","10");
            text=text.replace("november","11");
            text=text.replace("december","12");
            text=text.replace("st","");
            text=text.replace("nd","");
            text=text.replace("rd","");
            text=text.replace("th","");
            text=text.trim();
            return text;
        }
        return text;
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

