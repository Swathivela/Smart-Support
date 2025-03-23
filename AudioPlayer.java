package com.prjct.nyx; 
import androidx.appcompat.app.AppCompatActivity; 
import androidx.core.app.ActivityCompat; 
import androidx.core.content.ContextCompat; 
import android.annotation.SuppressLint; 
import android.content.pm.PackageManager; 
import android.media.MediaPlayer; 
import android.media.MediaRecorder; 
import android.os.Bundle; 
import android.os.Environment; 
import android.speech.tts.TextToSpeech; 
import android.util.Log; 
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView; 
import android.widget.Toast; 
import java.io.IOException; 
import java.util.Locale; 
import java.util.Random; 
import static android.Manifest.permission.RECORD_AUDIO; 
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE; 
public 
class 
RecordAudio 
TextToSpeech.OnInitListener{ 
extends 
AppCompatActivity 
Button buttonStart, buttonStop, buttonPlayLastRecordAudio, 
buttonStopPlayingRecording ; 
String AudioSavePathInDevice = null; 
MediaRecorder mediaRecorder ; 
Random random ; 
String RandomAudioFileName = "ABCDEFGHIJKLMNOP"; 
private TextView textview; 
public static final int RequestPermissionCode = 1; 
MediaPlayer mediaPlayer ; 
private Object BuildDev; 
TextToSpeech textToSpeech; 
@Override 
protected void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_record_audio); 
implements 
buttonStart = (Button) findViewById(R.id.button); 
buttonStop = (Button) findViewById(R.id.button2); 
buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3); 
buttonStopPlayingRecording = (Button)findViewById(R.id.button4); 
//  textview=findViewById(R.id.textView); 
textToSpeech = new TextToSpeech(this, this); 
buttonStop.setEnabled(false); 
buttonPlayLastRecordAudio.setEnabled(false); 
buttonStopPlayingRecording.setEnabled(false); 
random = new Random(); 
texttoSpeak(); 
} 
} 
