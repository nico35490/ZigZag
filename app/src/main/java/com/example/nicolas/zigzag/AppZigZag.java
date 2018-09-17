package com.example.nicolas.zigzag;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class AppZigZag extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        ZigZagView zigZagView = new ZigZagView(this);
        setContentView(zigZagView);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.game_song);
        mediaPlayer.setVolume(0.5f,0.5f);
        mediaPlayer.start();

    }
    public void lose(int score){
        //Intent intent = new Intent(AppZigZag.this, MenuZigZag.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //startActivity(intent);
        mediaPlayer.stop();
        mediaPlayer.release();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("score",score);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        mediaPlayer.release();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



}
