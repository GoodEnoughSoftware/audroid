package com.goodenoughsoftware.audroid;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.io.File;

/**
 * A class that provides methods for recording and playing audio on Android. Note that the user
 * of this class is responsible for remembering where audio files were saved
 * Last modified July 21, 2016
 * @author Aaron Vontell
 * @version 0.1.1
 */
public class Audroid {

    private File audioFile;
    private AudroidSource sourceDevice;
    private boolean recording = false;

    /**
     * Creates an object that will handle the recording and playback of audio to and from a given
     * file. Recording will be done through the given source.
     * *Note: this file requires permissions for reading storage, writing to storage, and
     * recording audio.
     * @param audioLocation The path / location that you wish to save and play the recording from
     * @param source The device that will listen to audio for recording
     */
    @RequiresPermission(allOf = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO})
    public Audroid(@NonNull File audioLocation, @NonNull AudroidSource source) {

        this.audioFile = audioLocation;
        this.sourceDevice = source;

    }

    /**
     * Starts the audio recording (asynchronously as a background service), saving the information
     * to the file given at Audroid creation.
     * *Note: this will overwrite any content at the given audioLocation
     */
    public void startRecording() {

        recording = true;
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Pause the recording
     */
    public void pauseRecording() {

        recording = false;
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Continue the recording
     */
    public void unpauseRecording() {

        recording = true;
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Stops the recording, ensuring that the audio file is saved and is a valid mp3 file
     */
    public void stopRecording() {

        recording = false;
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Changes the location to save the file to newLocation
     * *Note: This method cannot be called once startRecording() has been called and before
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
                this.audioFile = newLocation;

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
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Deletes the file at the audioLocation given at Audroid creation, and returns true if this
     * file actually existed in the first place
     * @return Whether this file existed in the file system
     */
    public boolean deleteRecording() {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Returns the File that holds the audio information (if audio has yet been recorder)
     * @return the audio file
     */
    public File getSavedFile() {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * Returns the recorded audio file as an mp3
     * TODO: @return The mp3 audio file
     */
    public void getRecording() {
        throw new RuntimeException("Not yet implemented!");
    }

}
