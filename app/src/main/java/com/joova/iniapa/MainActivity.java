package com.joova.iniapa;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";

    private TextToSpeech tts;
    private TextView txtNama;
    private ImageView imgGambar;

    private int index = 0;

    private List<Integer> listImage = new ArrayList<Integer>();
    private List<String> listNama = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNama = findViewById(R.id.txtNama);
        imgGambar = findViewById(R.id.imgGambar);


        //load image ke array
        listImage.add(R.drawable.apel);
        listImage.add(R.drawable.bebek);
        listImage.add(R.drawable.cicak);
        listImage.add(R.drawable.durian);
        listImage.add(R.drawable.eskrim);
        listImage.add(R.drawable.film);
        listImage.add(R.drawable.gunung);
        listImage.add(R.drawable.harimau);
        listImage.add(R.drawable.ikan);
        listImage.add(R.drawable.jam);
        listImage.add(R.drawable.kucing);
        listImage.add(R.drawable.laut);
        listImage.add(R.drawable.monyet);
        listImage.add(R.drawable.nasi);
        listImage.add(R.drawable.obat);

        // load nama ke array
        listNama.add(getString(R.string.apel));
        listNama.add(getString(R.string.bebek));
        listNama.add("Cicak");
        listNama.add("Durian");
        listNama.add("Es krim");
        listNama.add("Film");
        listNama.add("Gunung");
        listNama.add("Harimau");
        listNama.add("Ikan");
        listNama.add("Jam");
        listNama.add("Kucing");
        listNama.add("Laut");
        listNama.add("Monyet");
        listNama.add("Nasi");
        listNama.add("Obat");

        // set nama & gambar pertama di load
        txtNama.setText(listNama.get(index));
        imgGambar.setImageResource(listImage.get(index));

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED ){
                        CharSequence text = "Error TTS Tidak support.";
                        Log.e(TAG, text.toString());
                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, text ,Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    CharSequence text = "Inisialisasi TTS Gagal.";
                    Log.e(TAG, text.toString());
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, text ,Toast.LENGTH_SHORT);
                    toast.show();
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
                    bicara(getString(R.string.pesan_bener));
                    index++;
                    Log.d(TAG, "index="+ index);
                    if(index >= listImage.size()){
                        bicara(getString(R.string.pesan_selesai));
                        index = 0;
                    }
                    gambarBerikutnya();
                } else {
                    bicara(getString(R.string.pesan_salah));
                }

            }
        }
    }

    void gambarBerikutnya(){
        txtNama.setText(listNama.get(index));
        imgGambar.setImageResource(listImage.get(index));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.shutdown();
        }
    }
}
