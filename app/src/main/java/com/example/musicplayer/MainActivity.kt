package com.example.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var startTime : Double = 0.0;
    var finalTime : Double = 0.0;
    var forwardTime = 10000;
    var backwardTime = 10000;
    var oneTimeOnly = 0;

    lateinit var time_txt : TextView

    lateinit var mediaPLayer : MediaPlayer

    lateinit var seekbar : SeekBar

    val handler : Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val play_btn : Button= findViewById(R.id.play_button);
        val stop_button : Button = findViewById(R.id.pause_btn);
        val forward_btn : Button= findViewById(R.id.forward_btn);
        val back_btn : Button= findViewById(R.id.back_btn);

        val title_txt : TextView = findViewById(R.id.song_title)
        time_txt = findViewById(R.id.time_left_text)
        seekbar = findViewById(R.id.seekBar)

        //Media pLayer
         mediaPLayer = MediaPlayer.create(
            this,
            R.raw.pathaan_theme
        )

        seekbar.isClickable = false

        //Adding Functionalities for the button
        play_btn.setOnClickListener(){
            mediaPLayer.start()

            finalTime = mediaPLayer.duration.toDouble()
            startTime = mediaPLayer.currentPosition.toDouble()

            if (oneTimeOnly == 0){
                seekbar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            time_txt.text = startTime.toString()
            seekbar.setProgress(startTime.toInt())

            handler.postDelayed(UpdateSongTime, 100)
        }

        //Setting the Music Title
        title_txt.text = "" + resources.getResourceEntryName(R.raw.pathaan_theme)

        //Stop Button
        stop_button.setOnClickListener(){
            mediaPLayer.pause()
        }

        //Forward Button
        forward_btn.setOnClickListener(){
            var temp =  startTime
            if ((temp + forwardTime) <= finalTime){
                startTime = startTime + forwardTime
                mediaPLayer.seekTo(startTime.toInt());
            }else{
                Toast.makeText(this,
                    "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
            }
        }

        //Back Button
        back_btn.setOnClickListener(){
            var temp = startTime.toInt()

            if ((temp - backwardTime) > 0){
                startTime = startTime - backwardTime;
                mediaPLayer.seekTo(startTime.toInt());
            }else{
                Toast.makeText(this,
                    "Can't Go Back!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //Creating the runnable
    val UpdateSongTime: Runnable = object : Runnable{
        override fun run() {
            startTime = mediaPLayer.currentPosition.toDouble()
            time_txt.text = "" +
                    String.format(
                        "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()
                        - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()
                            )
            ))
                    )

            seekbar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }
}