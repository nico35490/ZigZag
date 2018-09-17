package com.example.nicolas.zigzag;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by Nicolas on 19/11/2016.
 */

public class SettingsZigZag extends AppCompatActivity {
    private final static String PREFS = "PREFS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_zigzag);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        Typeface font = Typeface.createFromAsset(getAssets(), "BebasNeue.otf");
        List<TextView> tvs = new ArrayList<>();
        tvs.add((TextView) findViewById(R.id.settingsTitle));
        ImageButton returnButton = (ImageButton) findViewById(R.id.returnButton) ;
        //elements settings
        tvs.add((TextView) findViewById(R.id.colorPathSettingTilte));
        tvs.add((TextView) findViewById(R.id.colorPathSettingText));
        ImageButton colorPathSettingButton = (ImageButton) findViewById(R.id.colorPathSettingButton);
        tvs.add((TextView) findViewById(R.id.colorSideSettingTilte));
        tvs.add((TextView) findViewById(R.id.colorSideSettingText));
        ImageButton colorSideSettingButton = (ImageButton) findViewById(R.id.colorSideSettingButton);
        tvs.add((TextView) findViewById(R.id.colorBallSettingTilte));
        tvs.add((TextView) findViewById(R.id.colorBallSettingText));
        ImageButton colorBallSettingButton = (ImageButton) findViewById(R.id.colorBallSettingButton);
        Button validerButton = (Button) findViewById(R.id.validerButton);
        Button retablirButton = (Button) findViewById(R.id.retablirButton);


        for(TextView txt : tvs)
            txt.setTypeface(font);

        colorPathSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog("pathcolor",ZigZagView.DEFAULT_PATHCOLOR);
            }
        });
        colorSideSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog("sidecolor",ZigZagView.DEFAULT_SIDECOLOR);
            }
        });
        colorBallSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog("ballcolor",ZigZagView.DEFAULT_BALLCOLOR);
            }
        });
        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        retablirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(PREFS,0).edit().clear().commit();
                Toast.makeText(v.getContext(), "Les couleurs par défaut ont été rétablis", Toast.LENGTH_SHORT).show();
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //faire bouton retablir qui remove tout le sharedPreference PREFS
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    // utilisation de l'api Chroma (voir gradle)
    // apparation d'une fenetre pour choisir une couleur
    // enregirstre dans les préferences la couleur choisi la clé donnée en parametre
    // le default color donné en paramètre sert a initialiser la couleur à
    // la couleur donnée si aucune donnée n'est donnée dans les préférences
    private void showColorPickerDialog(final String key, final int defaultcolor) {
        new ChromaDialog.Builder()
                .initialColor(getSharedPreferences(PREFS,0).getInt(key,defaultcolor))
                .colorMode(ColorMode.RGB)
                .onColorSelected(new ColorSelectListener() {
                    @Override public void onColorSelected(int color) {
                        getSharedPreferences(PREFS,0).edit().putInt(key,color).commit();
                    }
                })
                .create()
                .show(getSupportFragmentManager(), "dialog");
    }

}
