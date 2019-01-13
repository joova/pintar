package com.joova.iniapa;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";

    private TextToSpeech tts;
    TextView txtNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNama = findViewById(R.id.txtNama);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED ){
                        Log.e(TAG, "Error TTS Tidak support.");
                    }

                } else {
                    Log.e(TAG, "Inisialisasi TTS Gagal.");
                }
            }
        });

    }

    public void mulaiBaca(View view) {
        CharSequence nama = txtNama.getText();
        bicara(nama);

    }

    public void bicara(CharSequence kata) {
        tts.speak(kata, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void mulaiTirukan(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ayo tirukan!");

        startActivityForResult(intent, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3000) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String tirukan = results.get(0);
                CharSequence nama = txtNama.getText();

                if(tirukan.equalsIgnoreCase(nama.toString())){
                    bicara("Anda benar");
                } else {
                    bicara("Kurang Tepat, Ulangi lagi");
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.shutdown();
        }
    }
}
