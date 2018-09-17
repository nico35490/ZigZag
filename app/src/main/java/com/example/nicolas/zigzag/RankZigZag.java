package com.example.nicolas.zigzag;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Nicolas on 17/11/2016.
 */

public class RankZigZag extends AppCompatActivity {
    private List<TextView> ranks;
    private List<TextView> scores;
    private List<LinearLayout> rows;
    private LinearLayout rankings;
    private Typeface font;
    private final static String SCORE_PREF = "SCORES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_zigzag);
        // mise en place de l'animation de l'activité
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        // initailisation des éléments du XML layout
        ImageButton returnButton = (ImageButton) findViewById(R.id.returnButton);
        ranks = new ArrayList<TextView>();
        scores = new ArrayList<TextView>();
        rows = new ArrayList<LinearLayout>();
        ranks.add((TextView)findViewById(R.id.rank1));
        ranks.add((TextView)findViewById(R.id.rank2));
        ranks.add((TextView)findViewById(R.id.rank3));
        ranks.add((TextView)findViewById(R.id.rank4));
        ranks.add((TextView)findViewById(R.id.rank5));
        ranks.add((TextView)findViewById(R.id.rank6));
        ranks.add((TextView)findViewById(R.id.rank7));
        ranks.add((TextView)findViewById(R.id.rank8));
        ranks.add((TextView)findViewById(R.id.rank9));
        ranks.add((TextView)findViewById(R.id.rank10));
        scores.add((TextView)findViewById(R.id.score1));
        scores.add((TextView)findViewById(R.id.score2));
        scores.add((TextView)findViewById(R.id.score3));
        scores.add((TextView)findViewById(R.id.score4));
        scores.add((TextView)findViewById(R.id.score5));
        scores.add((TextView)findViewById(R.id.score6));
        scores.add((TextView)findViewById(R.id.score7));
        scores.add((TextView)findViewById(R.id.score8));
        scores.add((TextView)findViewById(R.id.score9));
        scores.add((TextView)findViewById(R.id.score10));
        rows.add((LinearLayout)findViewById(R.id.row1));
        rows.add((LinearLayout)findViewById(R.id.row2));
        rows.add((LinearLayout)findViewById(R.id.row3));
        rows.add((LinearLayout)findViewById(R.id.row4));
        rows.add((LinearLayout)findViewById(R.id.row5));
        rows.add((LinearLayout)findViewById(R.id.row6));
        rows.add((LinearLayout)findViewById(R.id.row7));
        rows.add((LinearLayout)findViewById(R.id.row8));
        rows.add((LinearLayout)findViewById(R.id.row9));
        rows.add((LinearLayout)findViewById(R.id.row10));

        // définr la police
        TextView rankTitle = (TextView) findViewById(R.id.rankTitle);
        font = Typeface.createFromAsset(getAssets(), "BebasNeue.otf");

        rankTitle.setTypeface(font);
        // récupération du top 10
        SharedPreferences scorePref = getSharedPreferences(SCORE_PREF,0);
        for (int i = 0; i<=9; i++)
            scores.get(i).setText(String.valueOf(scorePref.getInt(String.valueOf(i+1),0))+"pts");

        // initialisation du bouton retour
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        long i = 50;
        long j = 5;
        for(TextView txt : ranks){
            txt.setTypeface(font);
            txt.setTextSize(TypedValue.COMPLEX_UNIT_PX , txt.getHeight()*2/3);
        }
        for(TextView txt : scores){
            txt.setTypeface(font);
            txt.setTextSize(TypedValue.COMPLEX_UNIT_PX , txt.getHeight()*2/3);
        }
        for (LinearLayout l : rows){
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
            j+=5;
            i+=50+j;
            animation.setStartOffset(i);
            l.startAnimation(animation);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
