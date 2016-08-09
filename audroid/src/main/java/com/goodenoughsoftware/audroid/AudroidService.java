package com.goodenoughsoftware.audroid;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * A service that provides methods for recording and playing audio on Android. Note that the user
 * of this class is responsible for remembering where audio files were saved.
 * THIS CLASS SHOULD NOT BE USED DIRECTLY; INSTEAD, USE THE `AUDROID` CLASS
 * Last modified August 7, 2016
 * @author Aaron Vontell
 * @version 0.3.2
 */
public class AudroidService extends Service {

    private boolean recording = false;
    private boolean playing = false;
    private MediaRecorder audioRecorder;
    private MediaPlayer audioPlayer;
    private String privateFilePath;

    public final static String START_RECORDING = "com.goodenoughsoftware.audroid.START_RECORDING";
    public final static String PAUSE_RECORDING = "com.goodenoughsoftware.audroid.PAUSE_RECORDING";
    public final static String RESUME_RECORDING = "com.goodenoughsoftware.audroid.RESUME_RECORDING";
    public final static String STOP_RECORDING = "com.goodenoughsoftware.audroid.STOP_RECORDING";
    public final static String START_PLAYING = "com.goodenoughsoftware.audroid.START_PLAYING";
    public final static String PAUSE_PLAYING = "com.goodenoughsoftware.audroid.PAUSE_PLAYING";
    public final static String RESUME_PLAYING = "com.goodenoughsoftware.audroid.RESUME_PLAYING";
    public final static String STOP_PLAYING = "com.goodenoughsoftware.audroid.STOP_PLAYING";

    public final static String FILE_PATH = "com.goodenoughsoftware.audroid.extra.STOP_PLAYING";

    /**
     * This is a default constructor that should not be used directly
     */
    public AudroidService() { }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Decide what sort of action is being made (either starting the recording process,
        // pausing the recording, resuming the recording,
        String action = intent.getAction();
        // TODO: Better error checks here

        // If creating for the first time, instantiate with a file name
        if(action == null) {

            String filePath = intent.getStringExtra(FILE_PATH);
            this.privateFilePath = filePath;

        } else {
            switch(action) {
                case START_RECORDING:
                    if(!recording) {startRecording();}
                    break;
                case PAUSE_RECORDING:
                    if(recording) {pauseRecording();}
                    break;
                case RESUME_RECORDING:
                    if(!recording) {resumeRecording();}
                    break;
                case STOP_RECORDING:
                    stopRecording();
                    break;
                case START_PLAYING:
                    if(!playing) {playRecording();}
                    break;
                case PAUSE_PLAYING:
                    if(playing) {pausePlayingRecording();}
                    break;
                case RESUME_PLAYING:
                    if(!playing) {resumePlayingRecording();}
                    break;
                case STOP_PLAYING:
                    stopPlayingRecording();
                    break;
            }
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**Slices
     * Starts the audio recording (asynchronously as a background service), saving the information
     * to a random file in internal storage.
     * *Note: this will overwrite any content at the given audioLocation
     * TODO: Should this handle IOException, or throw them?
     */
    public void startRecording() {

        try {
            audioRecorder = new MediaRecorder();
            // TODO: Set correct audio source
            audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            audioRecorder.setOutputFile(privateFilePath);
            audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            audioRecorder.prepare();
            audioRecorder.start();

            recording = true;
        } catch (IOException e) {
            throw new RuntimeException("Start recording failed: " + e.getMessage());
        }


    }

    /**
     * Pause the recording
     */
    public void pauseRecording() {

        // TODO: This needs to use mp4 appending with stop and start
        recording = false;
        audioRecorder.pause();

    }

    /**
     * Resume the recording
     */
    public void resumeRecording() {

        recording = true;
        audioRecorder.resume();

    }

    /**
     * Stops the recording, ensuring that the audio file is saved and is a valid mp3 file
     */
    public void stopRecording() {

        recording = false;
        audioRecorder.stop();

    }

    /**
     * Plays back the recording
     */
    public void playRecording() {
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(privateFilePath);
            audioPlayer.prepare();
            audioPlayer.start();
        } catch (Exception e) {

        }
    }

    /**
     * Stops the playback of the recording
     */
    public void stopPlayingRecording() {
        audioPlayer.stop();
    }

    /**
     * Pauses the current playback of the recording
     */
    public void pausePlayingRecording() {
        audioPlayer.pause();
    }

    /**
     * Resumes the current playback of the recording
     */
    public void resumePlayingRecording() {
        audioPlayer.start();
    }

}
