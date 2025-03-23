package com.prjct.nyx; 
import android.content.Intent; 
import android.os.Bundle; 
import android.os.Handler; 
import androidx.appcompat.app.AppCompatActivity; 
public class Splash extends AppCompatActivity { 
Handler handler; 
@Override 
protected void onCreate(Bundle savedInstanceState) { 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_splash); 
handler = new Handler(); 
handler.postDelayed(() -> { 
Intent intent = new Intent(Splash.this, InstructionsActivity.class); 
startActivity(intent); 
finish(); 
}, 3000); 
} 
}
