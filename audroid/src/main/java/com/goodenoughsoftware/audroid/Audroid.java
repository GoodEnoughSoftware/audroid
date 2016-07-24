package com.goodenoughsoftware.audroid;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

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
    private MediaRecorder audioRecorder;
    private int randomInt;
    private String privateFilePath;

    /**
     * Creates an object that will handle the recording and playback of audio to and from a given
     * file. Recording will be done through the given source.
     * * Note: this file requires permissions for reading storage, writing to storage, and
     * recording audio.
     * @param source The device that will listen to audio for recording
     */
    @RequiresPermission(allOf = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO})
    public Audroid(@NonNull AudroidSource source) {

        this.sourceDevice = source;

        while(this.privateFilePath == null || fileExists()) {
            this.randomInt = new Random().nextInt();
            this.privateFilePath = getFilesDir().getAbsolutePath() + "/" + randomInt + ".mp3";
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Starts the audio recording (asynchronously as a background service), saving the information
     * to a random file in internal storage.
     * *Note: this will overwrite any content at the given audioLocation
     * TODO: Should this handle IOException, or throw them?
     */
    public void startRecording() {

        try {
            audioRecorder = new MediaRecorder();
            audioRecorder.setAudioSource(sourceDevice.getAndroidSource());
            audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            // TODO; Check the temp file doesnt already exists
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
     * Changes the location to save the file to newLocation
     * * Note: This method cannot be called once startRecording() has been called and before
     * stopRecording() has been called, and will throw a RuntimeException if attempted
     * @param newLocation The new location of the audio file
     */
    public void setLocation(@NonNull File newLocation) {

        // Check to see if the recording is in process
        if(!recording) {

            // If not, change the file location
            if(fileExists()) {

                // If the file already exists, copy and paste it into the new location

            } else {

                // If the recording was never made, reset the location
                // this.audioFile = newLocation;

            }
            throw new RuntimeException("Not yet implemented!");

        } else {

            // If so, tell the user they cannot do that!
            throw new RuntimeException("File location cannot change while recording");

        }

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
     * Returns the duration of the audio file
     * @return The duration of the audio file, in milliseconds, or -1 if the operation failed
     */
    public long getDuration() {

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

    }

}
