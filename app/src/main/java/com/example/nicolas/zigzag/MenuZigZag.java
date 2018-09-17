package com.example.nicolas.zigzag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nicolas on 27/10/2016.
 */

public class MenuZigZag extends AppCompatActivity {
    private final static int RETURN_SCORE = 1;
    private final static String SCORE_PREF = "SCORES";
    private TextView titletop, titlebot, lastScore, scoreView;
    private Button playButton;
    private ImageButton rankButton, aboutButton, settingsButton;
    private Typeface font;
    private ImageView starView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_zig_zag);
        // initialisation des éléments du xml Layout
        titletop = (TextView) findViewById(R.id.titletop);
        titlebot = (TextView) findViewById(R.id.titlebot);
        lastScore = (TextView) findViewById(R.id.lastScore);
        scoreView = (TextView) findViewById(R.id.score);
        playButton = (Button) findViewById(R.id.playButton);
        rankButton = (ImageButton) findViewById(R.id.rankButton);
        aboutButton = (ImageButton) findViewById(R.id.aboutButton);
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        starView = (ImageView) findViewById(R.id.starView);
        // changement de la police
        font = Typeface.createFromAsset(getAssets(), "square.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "Forced Square.ttf");
        titletop.setTypeface(font);
        titlebot.setTypeface(font);
        playButton.setTypeface(font);
        lastScore.setTypeface(font2);
        scoreView.setTypeface(font2);

        // changement de l'naimation de l'activité
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        // création des listener des bouton qui dirigent vers diférente activité
        playButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MenuZigZag.this, AppZigZag.class);
               // cette activité attent un resutat (le score), retourné par l'activité AppZigZAg
               startActivityForResult(intent, RETURN_SCORE);
            }
       });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuZigZag.this, AboutZigZag.class);
                startActivity(intent);
            }
        });
        rankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuZigZag.this, RankZigZag.class);
                startActivity(intent);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuZigZag.this, SettingsZigZag.class);
                startActivity(intent);
            }
        });

    }
    // méthode appeler lors dans la fermeture de AppZigZag.
    // Si un resultat est retourné, recupérer le score et l'inscrire dans les preférence des scores
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RETURN_SCORE) {
            if(resultCode == Activity.RESULT_OK){
                int score=data.getIntExtra("score",0);
                // si top 10, rendre l'étoile visible
                if(insertScore(score))
                    starView.setVisibility(View.VISIBLE);
                else starView.setVisibility(View.INVISIBLE);
                lastScore.setText(R.string.lastScore);
                scoreView.setText(String.valueOf(score));
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
    // retourne vrai si top 10
    // insert le score au bon endroit dans les sharedPreferences
    private boolean insertScore(int score) {
        SharedPreferences scores = getSharedPreferences(SCORE_PREF, 0);
        SharedPreferences.Editor editor = scores.edit();
        int i=11;
        // recherche de l'emplacement du nouveau score
        while(i>1){
            if(scores.getInt(String.valueOf(i-1),0)<score)
                i--;
            else
                break;
        }
        // décale le tableau des score de 1 rang pour insérer le nouveau score
        for(int j = 10; j>=i;j--){
            if (j!=10) {
                editor.putInt(String.valueOf(j + 1), scores.getInt(String.valueOf(j), 0));
            }
        }
        //ajoute le nouveau score au bon emplacement
        editor.putInt(String.valueOf(i), score);
        editor.commit();
        if (i<=10)
            return true;
        return false;
    }
    // responsivité des taille de text du menu
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        playButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, playButton.getHeight()/2);
        titletop.setTextSize(TypedValue.COMPLEX_UNIT_PX,titletop.getWidth()/8);
        titlebot.setTextSize(TypedValue.COMPLEX_UNIT_PX,titlebot.getWidth()/9);
    }
}
