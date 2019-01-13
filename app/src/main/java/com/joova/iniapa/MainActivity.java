package com.joova.iniapa;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
        tts.speak(nama, TextToSpeech.QUEUE_FLUSH, null, null);

    }

    public void mulaiTirukan(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.shutdown();
        }
    }
}
