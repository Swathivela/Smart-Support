package com.prjct.nyx; 
import androidx.annotation.Nullable; 
= 
 
import androidx.appcompat.app.AppCompatActivity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.speech.RecognizerIntent; 
import android.speech.tts.TextToSpeech; 
import android.util.Log; 
import android.view.View; 
import android.widget.ImageView; 
import android.widget.TextView; 
import android.widget.Toast; 
import java.util.ArrayList; 
import java.util.Locale; 
public 
class 
SpeechToText 
extends 
TextToSpeech.OnInitListener { 
private TextView tv_Speech_to_text; 
private ImageView mic; 
AppCompatActivity 
private static final int REQUEST_CODE_SPEECH_INPUT = 1; 
private TextToSpeech textToSpeech; 
@Override 
protected void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_speech_to_text); 
mic = findViewById(R.id.micimageView); 
tv_Speech_to_text = findViewById(R.id.textspeek); 
textToSpeech = new TextToSpeech(this, this); 
textToSpeak(); 
} 
public void getSpeechInput(View view) { 
implements 
Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); 
intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); 
intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); 
if (intent.resolveActivity(getPackageManager()) != null) { 
startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT); 
} else { 
Toast.makeText(this, "Your Device Doesn't Support Speech Input", 
Toast.LENGTH_SHORT).show(); 
} 
} 
 
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent 
data) { 
        super.onActivityResult(requestCode, resultCode, data); 
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == 
RESULT_OK && data != null) { 
            ArrayList<String> result = 
data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); 
            if (result != null && !result.isEmpty()) { 
                tv_Speech_to_text.setText(result.get(0)); 
            } 
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
                textToSpeak(); 
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
    private void textToSpeak() { 
        String text = "Please choose a mic, speak, and convert to text."; 
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null); 
    } 
} 
