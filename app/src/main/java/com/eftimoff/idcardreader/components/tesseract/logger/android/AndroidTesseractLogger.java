package com.eftimoff.idcardreader.components.tesseract.logger.android;

import android.util.Log;

import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLoggerLevel;

public class AndroidTesseractLogger implements TesseractLogger {

    private static final String TAG = AndroidTesseractLogger.class.getSimpleName();

    private boolean isLoggerEnabled = true;

    @Override
    public boolean isEnabled() {
        return isLoggerEnabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        isLoggerEnabled = enabled;
    }

    @Override
    public void log(final String message) {
        if (!isLoggerEnabled) {
            return;
        }
        log(TesseractLoggerLevel.INFO, message);
    }

    @Override
    public void log(final String message, final Object... args) {
        if (!isLoggerEnabled) {
            return;
        }
        log(TesseractLoggerLevel.INFO, String.format(message, args));
    }

    @Override
    public void error(final Throwable throwable) {
        if (!isLoggerEnabled) {
            return;
        }
        Log.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    public void log(final TesseractLoggerLevel tesseractLoggerLevel, final String message) {
        if (!isLoggerEnabled) {
            return;
        }
        switch (tesseractLoggerLevel) {
            case INFO:
                Log.i(TAG, message);
                break;
            case DEBUG:
                Log.d(TAG, message);
                break;
            case WARNING:
                Log.w(TAG, message);
                break;
            case ERROR:
                Log.e(TAG, message);
                break;
            default:
                break;
        }
    }
}
