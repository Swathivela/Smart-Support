package com.prjct.nyx; 
import androidx.appcompat.app.AppCompatActivity; 
import android.os.Build; 
import android.os.Bundle; 
import android.speech.tts.TextToSpeech; 
import android.util.Log; 
import android.view.View; 
import android.widget.EditText; 
import android.widget.ImageView; 
import java.util.Locale; 
public 
class 
TextToSpeak 
TextToSpeech.OnInitListener{ 
ImageView speakBtn; 
EditText speakText; 
TextToSpeech textToSpeech; 
@Override 
extends 
AppCompatActivity 
protected void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_text_to_speak); 
speakText = (EditText) findViewById(R.id.txtSpeak); 
speakBtn = (ImageView)findViewById(R.id.btnSpeech); 
textToSpeech = new TextToSpeech(this, this); 
speakBtn.setOnClickListener(new View.OnClickListener() { 
@Override 
public void onClick(View v) 
{ 
texttoSpeak(); 
} 
}); 
} 
@Override 
public void onInit(int status) { 
if (status == TextToSpeech.SUCCESS) { 
int result = textToSpeech.setLanguage(Locale.US); 
implements 
 
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
        String text = speakText.getText().toString(); 
        if ("".equals(text)) { 
            text = "Please enter some text to speak."; 
        } 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { 
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null); 
        } 
        else { 
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null); 
        } 
    } 
}
