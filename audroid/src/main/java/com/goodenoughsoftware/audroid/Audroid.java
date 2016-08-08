package com.goodenoughsoftware.audroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.io.File;
import java.util.Date;
import java.util.Random;

/**
 * An intent that handles the start and modification of audio
 * recording and playback
 * @author Aaron Vontell
 * @version 0.1
 * @date 8/07/2016
 */
public class Audroid {

    private Intent audroidIntent;
    private Context context;
    private String filePath;
    private Date dateCreated;
    private String title;

    // ---------------------------------------------------------------------------------------------
    // METHODS FOR INSTANTIATION
    // ---------------------------------------------------------------------------------------------

    /**
     * Creates an Audroid object with default properties (random file
     * name, using microphone as the source). Note that the operations in
     * this class require the activation of the RECORD_AUDIO permission.
     * @param context The context that is using this Audroid object
     */
    @RequiresPermission(allOf = {
            Manifest.permission.RECORD_AUDIO})
    public Audroid(Context context) {

        audroidIntent = new Intent(context, AudroidService.class);
        this.context = context;

        // Create the new file path
        int randomInt;
        while(this.filePath == null || fileExists()) {
            randomInt = new Random().nextInt();
            this.filePath = context.getFilesDir().getAbsolutePath()
                    + "/" + randomInt + ".mp3";
        }
        audroidIntent.putExtra(AudroidService.FILE_PATH, this.filePath);

        // Create the file path
        this.dateCreated = new Date();

        context.startService(audroidIntent);

    }

    /**
     * Creates an Audroid object from the given File, which will be
     * overridden in the case of recording, and played from in the
     * case of playback. Note that the operations in this class require
     * this activation of the RECORD_AUDIO permission.
     * @param context The context that is using this Audroid object
     * @param saveFile The file that you wish to record or play from (must be a valid file
     *                 path) Note that using a file on external storage requires the
     *                 WRITE_EXTERNAL_STORAGE permission
     */
    @RequiresPermission(allOf = {
            Manifest.permission.RECORD_AUDIO})
    public Audroid(Context context, File saveFile) {

        audroidIntent = new Intent(context, AudroidService.class);
        audroidIntent.putExtra(AudroidService.FILE_PATH, saveFile.getAbsolutePath());
        this.context = context;
        this.filePath = saveFile.getAbsolutePath();

        // Create the file path
        this.dateCreated = new Date();

        context.startService(audroidIntent);

    }

    // ---------------------------------------------------------------------------------------------
    // METHODS FOR RECORDING AND PLAYBACK
    // TODO: Add Javadoc documentation once exceptions are added
    // ---------------------------------------------------------------------------------------------

    public void startRecording() {

        audroidIntent.setAction(AudroidService.START_RECORDING);
        context.startService(audroidIntent);

    }

    public void pauseRecording() {

        audroidIntent.setAction(AudroidService.PAUSE_RECORDING);
        context.startService(audroidIntent);

    }

    public void resumeRecording() {

        audroidIntent.setAction(AudroidService.RESUME_RECORDING);
        context.startService(audroidIntent);

    }

    public void stopRecording() {

        audroidIntent.setAction(AudroidService.STOP_RECORDING);
        context.startService(audroidIntent);

    }

    public void startPlayback() {

        audroidIntent.setAction(AudroidService.START_PLAYING);
        context.startService(audroidIntent);

    }

    public void pausePlayback() {

        audroidIntent.setAction(AudroidService.PAUSE_PLAYING);
        context.startService(audroidIntent);

    }

    public void resumePlayback() {

        audroidIntent.setAction(AudroidService.RESUME_PLAYING);
        context.startService(audroidIntent);

    }

    public void stopPlayback() {

        audroidIntent.setAction(AudroidService.STOP_PLAYING);
        context.startService(audroidIntent);

    }

    // ---------------------------------------------------------------------------------------------
    // METHODS FOR META INFORMATION
    // ---------------------------------------------------------------------------------------------

    /**
     * Sets the title of this audio piece, for human-readable display
     * @param title The title of the audio piece
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    /**
     * Returns the title of the audio piece
     * @return The title of the audio piece
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the duration of the audio file
     * @return The duration of the audio file, in milliseconds
     */
    public long getDuration() {
        return (long) 0.0;
    }

    /**
     * Returns the date time that this recording started
     * @return The date and time that this recording started
     */
    public Date getTimestamp() {
        return this.dateCreated;
    }

    /**
     * Returns the File that holds the audio information (if audio has yet been recorder)
     * @return the audio file
     */
    public File getSavedFile() {
        if (fileExists()) {
            return new File(filePath);
        } else {
            throw new RuntimeException("File not yet created");
        }
    }

    /**
     * Deletes the file at the audioLocation given at AudroidService creation, and returns true if this
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
     * Returns true if the file to save to already exists
     * @return Whether the file is already being used (and as such would be overridden by
     *         a recording)
     */
    private boolean fileExists() {
        File f = new File(filePath);
        return f.exists() && !f.isDirectory();
    }

}
