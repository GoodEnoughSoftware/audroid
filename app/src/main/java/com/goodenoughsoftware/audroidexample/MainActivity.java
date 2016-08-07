package com.goodenoughsoftware.audroidexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goodenoughsoftware.audroid.Audroid;
import com.goodenoughsoftware.audroid.AudroidSource;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Audroid recorder;
    private File recording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.start_button);
        final Button stopButton = (Button) findViewById(R.id.stop_button);
        Button playButton = (Button) findViewById(R.id.play_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playRecording();
            }
        });

        recording = new File(getFilesDir().getAbsolutePath() + "/recording.mp3");

    }

    @Override
    protected void onResume() {
        super.onResume();
        recorder = new Audroid(AudroidSource.MICROPHONE, recording);
    }

    private void startRecording() {

        recorder.startRecording();

    }

    private void pauseRecording() {

        recorder.pauseRecording();

    }

    private void stopRecording() {

        recorder.stopRecording();

    }

    private void playRecording() {

        recorder.playRecording();

    }
}
