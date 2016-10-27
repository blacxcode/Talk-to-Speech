package com.blacxcode.tts.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blacxcode.tts.R;
import com.blacxcode.tts.fragment.InfoFragment;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by blacXcode on 10/27/2016.
 */

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private EditText tts_content;
    private FloatingActionButton tts_speak_fab;
    private FloatingActionButton tts_listen_fab;
    private Locale setLoc;
    private TextView setLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts_content = (EditText) findViewById(R.id.tts_content);
        tts_speak_fab = (FloatingActionButton) findViewById(R.id.tts_speak_fab);
        tts_listen_fab = (FloatingActionButton) findViewById(R.id.tts_listen_fab);
        setLang = (TextView) findViewById(R.id.setLang);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR || status == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getApplicationContext(), "error code : " + status, Toast.LENGTH_LONG).show();
                } else {
                    setLoc = Locale.JAPANESE;
                    setLang.setText(setLoc.toString() + "-japanese");
                    tts.setLanguage(setLoc);
                }
            }
        });

        tts_speak_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak ();
            }
        });

        tts_listen_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen();
            }
        });
    }

    private void speak () {
        String stts = tts_content.getText().toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(stts, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(stts, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void listen() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, setLoc);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ngomongo Cuk !!!...");

        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Perangkat Anda tidak mendukung speech recognition", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

        if(reqCode == 100) {
            if (resCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognize(inSpeech);
            }
        }
    }

    private void recognize(String text) {
        tts_content.setText(text);
    }

    @Override
    public void onDestroy () {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onResume () {
        if (tts == null) {
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(setLoc);
                    } else {
                        Toast.makeText(getApplicationContext(), "error code : " + status, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_jpn) {
            Toast.makeText(getApplicationContext(), "Bahasa Jepang Dipilih", Toast.LENGTH_LONG).show();
            setLoc = Locale.JAPANESE;
            setLang.setText(setLoc.toString() + "-japanese");
        }

        if (id == R.id.action_idn) {
            Toast.makeText(getApplicationContext(), "Bahasa Indonesia Dipilih", Toast.LENGTH_LONG).show();
            setLoc = new Locale("id");
            setLang.setText(setLoc.toString() + "-indonesia");
        }

        if (id == R.id.action_eng) {
            Toast.makeText(getApplicationContext(), "Bahasa Inggris Dipilih", Toast.LENGTH_LONG).show();
            setLoc = Locale.ENGLISH;
            setLang.setText(setLoc.toString() + "-english");
        }

        if (id == R.id.action_set) {
            Toast.makeText(getApplicationContext(), "Cek/Memasang TTS", Toast.LENGTH_LONG).show();
            installVoiceData();
            return true;
        }

        if (id == R.id.action_opset) {
            Intent  setting = new Intent(Settings.ACTION_SETTINGS);
            startActivity(setting);
            return true;
        }

        if (id == R.id.action_help) {
            // Opening the Dialog Bottom Sheet
            new InfoFragment().show(getSupportFragmentManager(), "Dialog");
            return true;
        }

        tts.setLanguage(setLoc);

        return super.onOptionsItemSelected(item);
    }

    private void installVoiceData() {
        int availability = tts.isLanguageAvailable(setLoc);
        switch (availability) {
            case TextToSpeech.LANG_AVAILABLE :
                Toast.makeText(getApplicationContext(), "Bahasa sudah tersedia", Toast.LENGTH_LONG).show();
                break;
            case TextToSpeech.LANG_NOT_SUPPORTED :
                Toast.makeText(getApplicationContext(), "Bahasa belum ada, mencoba memasang di perangkat...", Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                ArrayList<String> languages = new ArrayList<String>();
                languages.add("id-ID");
                intent.putStringArrayListExtra(TextToSpeech.Engine.EXTRA_CHECK_VOICE_DATA_FOR, languages);
                startActivity(intent);
                break;
            case TextToSpeech.LANG_MISSING_DATA :
                Toast.makeText(getApplicationContext(), "Data bahasa telah hilang/rusak", Toast.LENGTH_LONG).show();
                break;
            case TextToSpeech.LANG_COUNTRY_AVAILABLE :
                Toast.makeText(getApplicationContext(), "Bahasa sudah tersedia", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }
}
