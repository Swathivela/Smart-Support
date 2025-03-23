package com.prjct.nyx; 
import androidx.annotation.NonNull; 
import androidx.appcompat.app.AppCompatActivity; 
import androidx.core.app.ActivityCompat; 
import androidx.core.content.ContextCompat; 
 
import android.Manifest; 
import android.app.AlertDialog; 
import android.content.ContentValues; 
import android.content.Context; 
import android.content.Intent; 
import android.content.SharedPreferences; 
import android.content.pm.PackageManager; 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.net.Uri; 
import android.os.Build; 
import android.os.Bundle; 
import android.provider.MediaStore; 
import android.speech.RecognizerIntent; 
import android.speech.tts.TextToSpeech; 
import android.text.method.ScrollingMovementMethod; 
import android.util.DisplayMetrics; 
import android.util.Log; 
import android.util.SparseArray; 
import android.view.View; 
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 
import android.widget.Toast; 
import com.google.android.gms.vision.Frame; 
import com.google.android.gms.vision.text.TextBlock; 
import com.google.android.gms.vision.text.TextRecognizer; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.InputStream; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.Comparator; 
import java.util.List; 
import java.util.Locale; 
public 
class 
ScanToSpeak 
TextToSpeech.OnInitListener { 
extends 
AppCompatActivity  
private static final int REQUEST_GALLERY = 0; 
private static final int REQUEST_CAMERA = 1; 
implements 
 
    private static final int MY_PERMISSIONS_REQUESTS = 0; 
    private static final int REQUEST_CODE_SPEECH_INPUT = 1; 
    private static final String TAG = MainActivity.class.getSimpleName(); 
    private Uri imageUri; 
    private TextView detectedTextView; 
    private ImageView speak,voice; 
    TextToSpeech textToSpeech; 
    String detectedText; 
    SimpleAdapter adapter; 
    int position; 
    int noteID; 
    @Override 
    public void onRequestPermissionsResult(int requestCode, 
                                           @NonNull String permissions[], @NonNull int[] grantResults) { 
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); 
        switch (requestCode) { 
            case MY_PERMISSIONS_REQUESTS: { 
                if (grantResults.length > 0 
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) { 
                } else { 
                    // FIXME: Handle this case the user denied to grant the permissions 
                } 
                break; 
            } 
            default: 
                // TODO: Take care of this case later 
                break; 
        } 
    } 
    private void requestPermissions() 
    { 
        List<String> requiredPermissions = new ArrayList<>(); 
        if (ContextCompat.checkSelfPermission(this, 
Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                != PackageManager.PERMISSION_GRANTED) { 
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE); 
        } 
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) { 
            requiredPermissions.add(Manifest.permission.CAMERA); 
 
 
        } 
        if (!requiredPermissions.isEmpty()) { 
            ActivityCompat.requestPermissions(this, 
                    requiredPermissions.toArray(new String[]{}), 
                    MY_PERMISSIONS_REQUESTS); 
        } 
    } 
    @Override 
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_scan_to_speak); 
        requestPermissions(); 
        findViewById(R.id.choose_from_gallery).setOnClickListener(new 
View.OnClickListener() { 
            @Override 
            public void onClick(View v) { 
                Intent intent = new Intent(); 
                intent.setType("image/*"); 
                intent.setAction(Intent.ACTION_GET_CONTENT); 
                startActivityForResult(intent, REQUEST_GALLERY); 
            } 
        }); 
        findViewById(R.id.take_a_photo).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View v) { 
                String filename = System.currentTimeMillis() + ".jpg"; 
                ContentValues values = new ContentValues(); 
                values.put(MediaStore.Images.Media.TITLE, filename); 
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); 
                imageUri = 
getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
values); 
                Intent intent = new Intent(); 
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE); 
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); 
                startActivityForResult(intent, REQUEST_CAMERA); 
            } 
        }); 
        detectedTextView = (TextView) findViewById(R.id.detected_text); 
        detectedTextView.setMovementMethod(new ScrollingMovementMethod()); 
 

        speak = findViewById(R.id.speak); 
        textToSpeech = new TextToSpeech(this, this); 
       // voice=findViewById(R.id.voice); 
        speak.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View v) 
            { 
                texttoSpeak(); 
 
            } 
        }); 
    } 
 
    private void inspectFromBitmap(Bitmap bitmap) { 
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build(); 
        try { 
            if (!textRecognizer.isOperational()) { 
                new AlertDialog. 
                        Builder(this). 
                        setMessage("Text recognizer could not be set up on your device").show(); 
                return; 
            } 
            Frame frame = new Frame.Builder().setBitmap(bitmap).build(); 
            SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame); 
            List<TextBlock> textBlocks = new ArrayList<>(); 
            for (int i = 0; i < origTextBlocks.size(); i++) { 
                TextBlock textBlock = origTextBlocks.valueAt(i); 
                textBlocks.add(textBlock); 
            } 
            Collections.sort(textBlocks, new Comparator<TextBlock>() { 
                @Override 
                public int compare(TextBlock o1, TextBlock o2) { 
                    int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top; 
                    int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left; 
                    if (diffOfTops != 0) { 
                        return diffOfTops; 
                    } 
                    return diffOfLefts; 
                } 
            }); 
 
 
 
            StringBuilder detectedText = new StringBuilder(); 
            for (TextBlock textBlock : textBlocks) { 
                if (textBlock != null && textBlock.getValue() != null) { 
                    detectedText.append(textBlock.getValue()); 
                    detectedText.append("\n"); 
                } 
            } 
            detectedTextView.setText(detectedText); 
        } 
        finally { 
            textRecognizer.release(); 
        } 
    } 
    private void inspect(Uri uri) { 
        InputStream is = null; 
        Bitmap bitmap = null; 
        try { 
            is = getContentResolver().openInputStream(uri); 
            BitmapFactory.Options options = new BitmapFactory.Options(); 
            options.inPreferredConfig = Bitmap.Config.ARGB_8888; 
            options.inSampleSize = 2; 
            options.inScreenDensity = DisplayMetrics.DENSITY_LOW; 
            bitmap = BitmapFactory.decodeStream(is, null, options); 
            inspectFromBitmap(bitmap); 
        } catch (FileNotFoundException e) { 
            Log.w(TAG, "Failed to find the file: " + uri, e); 
        } finally { 
            if (bitmap != null) { 
                bitmap.recycle(); 
            } 
            if (is != null) { 
                try { 
                    is.close(); 
                } catch (IOException e) { 
                    Log.w(TAG, "Failed to close InputStream", e); 
                } 
            } 
        } 
    } 
 
    
 
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        switch (requestCode) { 
            case REQUEST_GALLERY: 
                if (resultCode == RESULT_OK) { 
                    inspect(data.getData()); 
                } 
                break; 
            case REQUEST_CAMERA: 
                if (resultCode == RESULT_OK) { 
                    if (imageUri != null) { 
                        inspect(imageUri); 
                    } 
                } 
                break; 
 
                case 10: 
                    if(resultCode==RESULT_OK && data!=null){ 
                        ArrayList<String> 
result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); 
                       // detectedTextView.setText(result.get(0)); 
                        detectedText=result.get(0); 
                        
Toast.makeText(getApplicationContext(),detectedText,Toast.LENGTH_SHORT).show(); 
                    } 
                    break; 
            default: 
                super.onActivityResult(requestCode, resultCode, data); 
                break; 
        } 
    } 
 
    // text to speak 
 
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
        String text = detectedTextView.getText().toString(); 
        Log.e("textttttttttt",text); 
 
        SharedPreferences sharedPreferences = getSharedPreferences("spf", 
Context.MODE_PRIVATE); 
        SharedPreferences.Editor editor = sharedPreferences.edit(); 
        editor.putString("read",text); 
       /* editor.putInt("Count", 5);//say 
        for(int i=1; i<=5;i++) 
        { 
            editor.putString("read"+i,text); 
            Log.e("vvvv",text); 
        }*/ 
 
        editor.commit(); 
        editor.apply(); 
       /* editor.putInt("Count", 50);//say 
        for(int i=1; i<=50;i++) 
        { 
            editor.putString("Read"+i, "text"); 
 
 

        }*/ 
       // editor.commit(); 
        if ("".equals(text)) { 
            text = "Please Choose a Image or capture a image ."; 
        } 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { 
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null); 
        } else { 
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null); 
        } 
    } 
 
    // Declare the onBackPressed method when the back button is pressed this method will 
call 
   // @Override 
   /* public void onBackPressed() { 
        // Create the object of AlertDialog Builder class 
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanToSpeak.this); 
 
        // Set the message show for the Alert time 
        builder.setMessage("Do you Bookmark this page ?"); 
        textToSpeech.speak("Do you Bookmark this page ? yes? , no?", 
TextToSpeech.QUEUE_FLUSH, null); 
        // Set Alert Title 
        builder.setTitle("Alert !"); 
 
        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it 
will remain show 
        builder.setCancelable(false); 
        // Set the positive button with yes name Lambda OnClickListener method is use of 
DialogInterface interface. 
            builder.setPositiveButton("yes", (DialogInterface.OnClickListener) (dialog, which) 
> { 
                // When the user click yes button then app will close 
                Log.e("data",detectedText); 
                        if ("read".equalsIgnoreCase(detectedText)) { 
                            finish(); 
                            Log.e("aaaa","111"); 
                            Intent intent=new Intent(getApplicationContext(),Bookmark.class); 
                            startActivity(intent); 
 
 
                        } 
            }); 
 
            // Set the Negative button with No name Lambda OnClickListener method is use of 
DialogInterface interface. 
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> { 
                // If user click no then dialog box is canceled. 
                dialog.cancel(); 
            }); 
 
        // Create the Alert dialog 
        AlertDialog alertDialog = builder.create(); 
        // Show the Alert Dialog box 
        alertDialog.show(); 
    } 
*/ 
 
 
    // voice to text 
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
