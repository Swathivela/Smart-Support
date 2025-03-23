package com.prjct.nyx; 
import androidx.annotation.Nullable; 
import androidx.appcompat.app.AppCompatActivity; 
import android.content.Context; 
import android.content.Intent; 
import android.content.SharedPreferences; 
import android.os.Bundle; 
import android.speech.RecognizerIntent; 
import android.speech.tts.TextToSpeech; 
import android.text.Editable; 
import android.text.TextWatcher; 
import android.util.Log; 
import android.view.View; 
import android.widget.ImageView; 
import android.widget.TextView; 
import android.widget.Toast; 
import java.util.ArrayList; 
import java.util.HashSet; 
import java.util.Locale; 
  
public class Notes extends AppCompatActivity implements TextToSpeech.OnInitListener{ 
    private TextView tv_Speech_to_text; 
    private ImageView mic; 
    private static final int REQUEST_CODE_SPEECH_INPUT = 1; 
    int noteID; 
    TextToSpeech textToSpeech; 
    @Override 
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_notes); 
        mic = findViewById(R.id.micimageView); 
        tv_Speech_to_text = findViewById(R.id.textspeek); 
        textToSpeech = new TextToSpeech(this, this); 
        texttoSpeak(); 
        Intent intent = getIntent(); 
        noteID = intent.getIntExtra("noteID", -1); 
        if (noteID != -1) { 
            tv_Speech_to_text.setText(AddingNotes.notes.get(noteID)); 
        } else { 
            AddingNotes.notes.add("");                // as initially, the note is empty 
            noteID = AddingNotes.notes.size() - 1; 
            AddingNotes.arrayAdapter.notifyDataSetChanged(); 
        } 
        tv_Speech_to_text.addTextChangedListener(new TextWatcher() { 
            @Override 
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
            } 
            @Override 
            public void onTextChanged(CharSequence s, int start, int before, int count) { 
                AddingNotes.notes.set(noteID, String.valueOf(s)); 
                AddingNotes.arrayAdapter.notifyDataSetChanged(); 
 
SharedPreferences sharedPreferences = 
getApplicationContext().getSharedPreferences("com.tanay.thunderbird.deathnote", 
Context.MODE_PRIVATE); 
                HashSet<String> set = new HashSet<>(AddingNotes.notes); 
                sharedPreferences.edit().putStringSet("notes", set).apply(); 
            } 
            @Override 
            public void afterTextChanged(Editable s) { 
 
   
 
            } 
        }); 
    } 
    public void getSpeechInput(View view) { 
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); 
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); 
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); 
        if (intent.resolveActivity(getPackageManager()) != null) { 
            startActivityForResult(intent, 10); 
        } else { 
            Toast.makeText(this, "Your Device Don't Support Speech Input", 
Toast.LENGTH_SHORT).show(); 
        } 
    } 
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { 
        super.onActivityResult(requestCode, resultCode, data); 
        switch (requestCode) { 
            case 10: 
                if (resultCode == RESULT_OK && data != null) { 
                    ArrayList<String> result = 
data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); 
                    tv_Speech_to_text.setText(result.get(0)); 
                } 
                break; 
        } 
    } 
    @Override 
    public void onInit(int status) { 
        if (status == TextToSpeech.SUCCESS) { 
            int result = textToSpeech.setLanguage(Locale.US); 
            if (result == TextToSpeech.LANG_MISSING_DATA || result == 
TextToSpeech.LANG_NOT_SUPPORTED) { 
                Log.e("error", "This Language is not supported"); 
            } else { 
                texttoSpeak(); 
            } 
        } else { 
 
  
            Log.e("error", "Failed to Initialize"); 
        } 
    } 
    @Override 
    public void onDestroy() { 
        if (textToSpeech != null) { 
            textToSpeech.stop(); 
            textToSpeech.shutdown(); 
        } 
        super.onDestroy(); 
    } 
    private void texttoSpeak() { 
        String text ="Please choose a mic , speak convert to text."; 
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null); 
    }
