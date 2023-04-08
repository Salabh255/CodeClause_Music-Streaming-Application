package com.example.meri_aawaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView previous,play,next;
    ArrayList<File> songs;
    MediaPlayer mediaplayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        previous = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList)bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);

        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaplayer = MediaPlayer.create(this,uri);
        mediaplayer.start();
        seekBar.setMax(mediaplayer.getDuration());


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaplayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try{
                    while(currentPosition<mediaplayer.getDuration()){
                        currentPosition= mediaplayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaplayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaplayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaplayer.start();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=0){
                    position=position-1;
                }
                else{
                    position=songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
                play.setImageResource(R.drawable.pause);
                mediaplayer.start();
                seekBar.setMax(mediaplayer.getDuration());
                seekBar.setProgress(0);
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=0){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
                play.setImageResource(R.drawable.pause);
                mediaplayer.start();
                seekBar.setMax(mediaplayer.getDuration());
                seekBar.setProgress(0);
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });


    }
}