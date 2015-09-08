package com.eftimoff.idcardreader.components.tesseract.logger;

public interface TesseractLogger {

    boolean isEnabled();

    void setEnabled(final boolean enabled);

    void log(final String message);

    void log(final String message, final Object... args);

    void error(final Throwable throwable);

    void log(final TesseractLoggerLevel tesseractLoggerLevel, final String message);
}
