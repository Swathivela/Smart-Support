package com.prjct.nyx; 
import static com.prjct.nyx.R.*; 
import androidx.annotation.Nullable; 
import androidx.appcompat.app.AppCompatActivity; 
import androidx.cardview.widget.CardView; 
 
import android.app.SearchManager; 
import android.content.Context; 
import android.content.Intent; 
import android.os.Bundle; 
import android.os.VibrationEffect; 
import android.os.Vibrator; 
import android.speech.RecognizerIntent; 
import android.view.View; 
import android.widget.EditText; 
import android.widget.ImageView; 
import android.widget.Toast; 
import com.prjct.nyx.audio.MainPage; 
import java.util.ArrayList; 
import java.util.Locale; 
public class Home extends AppCompatActivity { 
private CardView speechtotext, texttospeak, imagescantospeak, pdftospeak, Audio, 
CreateNotes; 
private EditText editTextInput; 
private ImageView mic; 
private static final int REQUEST_CODE_SPEECH_INPUT = 1; 
@Override 
protected void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_home); 
final 
Vibrator 
vibrator 
getSystemService(Context.VIBRATOR_SERVICE); 
texttospeak = findViewById(R.id.cardview1); 
speechtotext = findViewById(R.id.cardView2); 
imagescantospeak = findViewById(R.id.cardView3); 
pdftospeak = findViewById(R.id.cardView4); 
Audio = findViewById(R.id.cardview5); 
CreateNotes = findViewById(R.id.cardView6); 
mic = findViewById(R.id.micimageView); 
editTextInput = findViewById(R.id.editsearch); 
speechtotext.setOnClickListener(view -> { 
if 
= 
(android.os.Build.VERSION.SDK_INT 
android.os.Build.VERSION_CODES.O) { 
VibrationEffect 
vibrationEffect 
= 
(Vibrator) 
>= 
VibrationEffect.createOneShot(1000, 
VibrationEffect.DEFAULT_AMPLITUDE); 
vibrator.cancel(); 
vibrator.vibrate(vibrationEffect); 
52  
 
  53  
                startActivity(new Intent(getApplicationContext(), SpeechToText.class)); 
            } 
        }); 
        texttospeak.setOnClickListener(view -> { 
            if (android.os.Build.VERSION.SDK_INT >= 
android.os.Build.VERSION_CODES.O) { 
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1000, 
VibrationEffect.DEFAULT_AMPLITUDE); 
                vibrator.cancel(); 
                vibrator.vibrate(vibrationEffect); 
                startActivity(new Intent(getApplicationContext(), TextToSpeak.class)); 
            } 
        }); 
        imagescantospeak.setOnClickListener(view -> { 
            if (android.os.Build.VERSION.SDK_INT >= 
android.os.Build.VERSION_CODES.O) { 
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1000, 
VibrationEffect.DEFAULT_AMPLITUDE); 
                vibrator.cancel(); 
                vibrator.vibrate(vibrationEffect); 
                startActivity(new Intent(getApplicationContext(), ScanToSpeak.class)); 
            } 
        }); 
        pdftospeak.setOnClickListener(view -> { 
            if (android.os.Build.VERSION.SDK_INT >= 
android.os.Build.VERSION_CODES.O) { 
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1000, 
VibrationEffect.DEFAULT_AMPLITUDE); 
                vibrator.cancel(); 
                vibrator.vibrate(vibrationEffect); 
                startActivity(new Intent(getApplicationContext(), PdfToSpeak.class)); 
            } 
        }); 
        Audio.setOnClickListener(view -> { 
            if (android.os.Build.VERSION.SDK_INT >= 
android.os.Build.VERSION_CODES.O) { 
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1000, 
VibrationEffect.DEFAULT_AMPLITUDE); 
                vibrator.cancel(); 
                vibrator.vibrate(vibrationEffect); 
 
  54  
                startActivity(new Intent(getApplicationContext(), MainPage.class)); 
            } 
        }); 
        CreateNotes.setOnClickListener(view -> { 
            if (android.os.Build.VERSION.SDK_INT >= 
android.os.Build.VERSION_CODES.O) { 
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1000, 
VibrationEffect.DEFAULT_AMPLITUDE); 
                vibrator.cancel(); 
                vibrator.vibrate(vibrationEffect); 
                startActivity(new Intent(getApplicationContext(), AddingNotes.class)); 
            } 
        }); 
   } 
    public void onSearchClick(View v) { 
        try { 
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH); 
            String term = editTextInput.getText().toString(); 
            intent.putExtra(SearchManager.QUERY, term); 
            startActivity(intent); 
        } catch (Exception e) { 
            // TODO: handle exception 
        } 
        editTextInput.getText().clear(); 
    } 
    public void getSpeechInput(View view) { 
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); 
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); 
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()){ 
        if (intent.resolveActivity(getPackageManager()) != null) { 
            startActivityForResult(intent, 10); 
        } else { 
            Toast.makeText(this, "Your Device Doesn't Support Speech Input", 
Toast.LENGTH_SHORT).show(); 
        } 
    } 
 
    @Override 
{ 
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) 
super.onActivityResult(requestCode, resultCode, data); 
if (requestCode == 10 && resultCode == RESULT_OK && data != null) { 
ArrayList<String> 
result 
data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); 
editTextInput.setText(result.get(0)); 
} 
} 
}
