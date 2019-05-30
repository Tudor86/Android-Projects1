package com.example.speechrecognizer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editText1,editText2,editText3,editText4;
    Button btnSpeak1,btnSpeak2,btnSpeak3,btnSpeak4,btnSend;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    int lastPressed,nrCopii,selectedLanguage=1;

    private DatabaseReference mFirebaseDatabase,ChildrenRef;
    private FirebaseDatabase mFirebaseInstance;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        checkPermission();

        ChildrenRef=FirebaseDatabase.getInstance().getReference();
        ChildrenRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nrCopii=(int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference();


        editText1 = findViewById(R.id.editText1);
        btnSpeak1 = findViewById(R.id.buttonSpeak1);
        editText2 = findViewById(R.id.editText2);
        btnSpeak2 = findViewById(R.id.buttonSpeak2);
        editText3 = findViewById(R.id.editText3);
        btnSpeak3 = findViewById(R.id.buttonSpeak3);
        editText4 = findViewById(R.id.editText4);
        btnSpeak4 = findViewById(R.id.buttonSpeak4);
        btnSend = findViewById(R.id.buttonSend);

        Spinner spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.languages,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        selectedLanguage=1;
                        speechRecogFunction("en-US");
                        Toast.makeText(parent.getContext(), "English!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        selectedLanguage=2;
                        speechRecogFunction("it");
                        Toast.makeText(parent.getContext(), "Italian!", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        selectedLanguage=3;
                        speechRecogFunction("de");
                        Toast.makeText(parent.getContext(), "German!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        btnSpeak1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lastPressed=1;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText1.setHint("Value( ex.25.3lei , or $13.26)");
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

        btnSpeak4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lastPressed=4;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText4.setHint("Comment");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        editText4.setText("");
                        editText4.setHint("Listening...");
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }

                return false;
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bon bon=new Bon(editText1.getText().toString(),editText2.getText().toString(),editText3.getText().toString(),editText4.getText().toString());
                if( ( editText1.getText().toString() !=null && !editText1.getText().toString().isEmpty()) &&( editText2.getText().toString() !=null && !editText2.getText().toString().isEmpty()) && ( editText3.getText().toString() !=null && !editText3.getText().toString().isEmpty()))
                {   ChildrenRef=FirebaseDatabase.getInstance().getReference();



                ChildrenRef.child("").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nrCopii=(int)dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                    mFirebaseDatabase.child("bon"+(nrCopii+1)).setValue(bon);
                Toast.makeText(getApplicationContext(),"Sent to database!", Toast.LENGTH_SHORT).show();
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                }
                else
                    Toast.makeText(getApplicationContext(),"First three fields must be completed!", Toast.LENGTH_SHORT).show();


            }
        });

    }

    void speechRecogFunction(String language)
    {
        try{
        mSpeechRecognizer.destroy();
        mSpeechRecognizer=null;
        mSpeechRecognizerIntent=null;
    } catch (Exception e){
        Log.d("distrus","intrat");
    }

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(getApplicationContext(),"Intrat in onReadyForSpeech!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {
                Toast.makeText(getApplicationContext(),"Intrat in onBeginningOfSpeech!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Toast.makeText(getApplicationContext(),"Intrat in onRmsChanged!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Toast.makeText(getApplicationContext(),"Intrat in onBufferReceived!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEndOfSpeech() {
                Toast.makeText(getApplicationContext(),"Intrat in onEndOfSpeech!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error) {
                Toast.makeText(getApplicationContext(),"Intrat in onResults!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Toast.makeText(getApplicationContext(),"Intrat in onResults!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Toast.makeText(getApplicationContext(),"Intrat in onResults!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {

                Toast.makeText(getApplicationContext(),"Intrat in onResults!", Toast.LENGTH_SHORT).show();

                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null)
                    switch (lastPressed) {
                        case 1:
                            editText1.setText(processText(matches.get(0),1));
                            break;
                        case 2:
                            editText2.setText(processText(matches.get(0),2));
                            break;
                        case 3:
                            editText3.setText(processText(matches.get(0),3));
                            break;
                        case 4:
                            editText4.setText(processText(matches.get(0),4));
                            break;
                        default:
                            break;
                    }
            }
        });
    }

    public String processText(String text,int numar){
        if(numar==1)
        {
            text=text.toLowerCase();
            text=text.trim();
            if(text.contains("dollar")) {
                text = text.replaceAll("[^\\d.]", "");
                text=text.trim();
                text="$"+text;
            }

            if(text.contains("euro")) {
                text = text.replaceAll("[^\\d.]", "");
                text=text.trim();
                text="€"+text;
            }

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
            text=text.replace("of","");
            text=text.trim();

            try{
            if (text.split("\\.")[0].length()<2)
                text="0"+text;}
            catch (Exception e){

            }
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

