package com.example.nicolas.zigzag;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Nicolas on 07/11/2016.
 */

public class AboutZigZag extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_zigzag);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


        TextView rules1 = (TextView) findViewById(R.id.rules1);
        TextView rules2 = (TextView) findViewById(R.id.rules2);
        TextView rules3 = (TextView) findViewById(R.id.rules3);
        TextView aboutTitle = (TextView) findViewById(R.id.aboutTitle);
        ImageButton returnButton = (ImageButton) findViewById(R.id.returnButton) ;

        Typeface font = Typeface.createFromAsset(getAssets(), "BebasNeue.otf");
        rules1.setTypeface(font);
        rules2.setTypeface(font);
        rules3.setTypeface(font);
        aboutTitle.setTypeface(font);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
