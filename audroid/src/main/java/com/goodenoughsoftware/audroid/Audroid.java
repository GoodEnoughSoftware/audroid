package com.goodenoughsoftware.audroid;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * A service that provides methods for recording and playing audio on Android. Note that the user
 * of this class is responsible for remembering where audio files were saved
 * Last modified July 21, 2016
 * @author Aaron Vontell
 * @version 0.1.1
 */
public class Audroid extends Service {

    private AudroidSource sourceDevice;
    private boolean recording = false;
    private boolean playing = false;
    private MediaRecorder audioRecorder;
    private MediaPlayer audioPlayer;
    private int randomInt;
    private String privateFilePath;
    private Date dateCreated;

    public final String START_RECORDING = "com.goodenoughsoftware.audroid.START_RECORDING";
    public final String PAUSE_RECORDING = "com.goodenoughsoftware.audroid.PAUSE_RECORDING";
    public final String RESUME_RECORDING = "com.goodenoughsoftware.audroid.RESUME_RECORDING";
    public final String STOP_RECORDING = "com.goodenoughsoftware.audroid.STOP_RECORDING";
    public final String START_PLAYING = "com.goodenoughsoftware.audroid.START_PLAYING";
    public final String PAUSE_PLAYING = "com.goodenoughsoftware.audroid.PAUSE_PLAYING";
    public final String RESUME_PLAYING = "com.goodenoughsoftware.audroid.RESUME_PLAYING";
    public final String STOP_PLAYING = "com.goodenoughsoftware.audroid.STOP_PLAYING";

    /**
     * Creates an object that will handle the recording and playback of audio to and from a given
     * file. Recording will be done through the given source.
     * * Note: this file requires the recording audio permission.
     * @param source The device that will listen to audio for recording
     */
    @RequiresPermission(allOf = {
            Manifest.permission.RECORD_AUDIO})
    public Audroid(@NonNull AudroidSource source) {

        this.sourceDevice = source;
        this.dateCreated = new Date();

        while(this.privateFilePath == null || fileExists()) {
            this.randomInt = new Random().nextInt();
            this.privateFilePath = getFilesDir().getAbsolutePath() + "/" + randomInt + ".mp3";
        }

    }

    /**
     * Creates an Audroid object from an existing file that contains MP3 content, with any new
     * recording being done through the given source.
     * * Note: this file requires the recording audio permission.
     * @param source THe device that will listen to audio for recording
     * @param existingFile The existing file that contains mp3 content
     */
    @RequiresPermission(allOf = {
            Manifest.permission.RECORD_AUDIO})
    public Audroid(@NonNull AudroidSource source, File existingFile) {

        this.sourceDevice = source;

        this.randomInt = -1;
        this.privateFilePath = existingFile.getAbsolutePath();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Decide what sort of action is being made (either starting the recording process,
        // pausing the recording, resuming the recording,
        String action = intent.getAction();
        // TODO: Better error checks here
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
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Pauses the current playback of the recording
     */
    public void pausePlayingRecording() {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Resumes the current playback of the recording
     */
    public void resumePlayingRecording() {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Returns true if the given file to save to already exists
     * @return Whether this file is already being used (and as such would be overridden by
     *         a recording)
     */
    private boolean fileExists() {
        File f = new File(privateFilePath);
        return f.exists() && !f.isDirectory();
    }

    /**
     * Deletes the file at the audioLocation given at Audroid creation, and returns true if this
     * file actually existed in the first place, and false if it failed or the file dne
     * @return Whether this file existed in the file system
     */
    public boolean deleteRecording() {
        if (fileExists()) {
            return getSavedFile().delete();
        } else {
            return false;
        }
    }

    /**
     * Returns the File that holds the audio information (if audio has yet been recorder)
     * @return the audio file
     */
    public File getSavedFile() {
        if (fileExists()) {
            return new File(privateFilePath);
        } else {
            throw new RuntimeException("File not yet created");
        }
    }

    /**
     * Changes the file name to the given name
     * * Note: This method cannot be called once startRecording() has been called and before
     * stopRecording() has been called, and will throw a RuntimeException if attempted
     * @param name The file name, without the extension or directory path to it.
     * @return Whether the name change operation succeeded
     */
    public boolean changeFileName(String name) {

        if(!recording) {
            File existingFile = new File(privateFilePath);
            String newPath = getFilesDir().getAbsolutePath() + "/" + name + ".mp3";
            boolean success = existingFile.renameTo(new File(newPath));
            this.privateFilePath = newPath;
            return success;
        } else {
            throw new RuntimeException("Attempting to change file name while recording!");
        }

    }

    /**
     * Returns the duration of the audio file
     * @return The duration of the audio file, in milliseconds, or -1 if the operation failed
     */
    public long getDuration() {

        /*
        try {
            File file = new File(privateFilePath);
            AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
            Map properties = baseFileFormat.properties();
            Long duration = (Long) properties.get("duration");
            return duration;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        */
        return (long) 0.0;

    }

    /**
     * Returns the name of the file, without the preceding path or extension
     * @return the name of the mp3 file
     */
    public String getName() {

        return new File(privateFilePath).getName();

    }

    /**
     * Returns the date time that this recording started
     * @return The date and time that this recording started
     */
    public Date getTimestamp() {
        return this.dateCreated;
    }

}
