package com.goodenoughsoftware.audroid;

import android.media.MediaRecorder;

/**
 * An enumerated type that represents what device is being used to record audio
 * Last modified July 21, 2016
 * @author Aaron Vontell
 * @version 0.1
 */
public enum AudroidSource {

    MICROPHONE;

    public static int getAndroidSource(AudroidSource source) {
        switch (source) {
            case MICROPHONE:
                return MediaRecorder.AudioSource.MIC;
            default:
                return MediaRecorder.AudioSource.MIC;
        }
    }

}
