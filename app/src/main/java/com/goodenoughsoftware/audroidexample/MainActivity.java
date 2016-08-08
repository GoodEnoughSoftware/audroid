package com.goodenoughsoftware.audroidexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goodenoughsoftware.audroid.Audroid;

public class MainActivity extends AppCompatActivity {

    private Intent recorder;

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

        recorder = new Intent(this, Audroid.class);
        startService(recorder);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startRecording() {

        recorder.setAction(Audroid.START_RECORDING);
        startService(recorder);

    }

    private void pauseRecording() {

        recorder.setAction(Audroid.PAUSE_RECORDING);
        startService(recorder);

    }

    private void stopRecording() {

        recorder.setAction(Audroid.STOP_RECORDING);
        startService(recorder);

    }

    private void playRecording() {

        recorder.setAction(Audroid.START_PLAYING);
        startService(recorder);

    }
}
