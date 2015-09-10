package com.eftimoff.idcardreader.utils;

import android.os.Environment;

import com.google.common.base.Preconditions;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;

public class FileUtils {

    private static final String SD_CARD_TRAINED_DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/tessdata/";
    private static final int BUFFER = 2048;

    private FileUtils() {
    }

    public static Observable<String> downloadFileToSdCard(final Response response, final String fileName) {
        Preconditions.checkNotNull(fileName, "Filename must not be null.");
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    final String downloadFileToSdCard = downloadFileToSdCardUtil(response.getBody().in(), fileName);
                    subscriber.onNext(downloadFileToSdCard);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<String> downloadFileToSdCard(final InputStream inputStream, final String fileName) {
        Preconditions.checkNotNull(fileName, "Filename must not be null.");
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    final String downloadFileToSdCard = downloadFileToSdCardUtil(inputStream, fileName);
                    subscriber.onNext(downloadFileToSdCard);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private static String downloadFileToSdCardUtil(final InputStream inputStream, final String fileName) throws Exception {
        final File parentFile = new File(SD_CARD_TRAINED_DATA_PATH);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        final File sdCardFile = new File(SD_CARD_TRAINED_DATA_PATH, fileName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(sdCardFile);
            copyFile(inputStream, fileOutputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return sdCardFile.getAbsolutePath();
    }

    private static void copyFile(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static Observable<String> unTarFileExtractAndDeleteTar(final String sdCardPath, final String desiredFileName) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    final String filePath = unTarFileAndExtract(sdCardPath, desiredFileName);
                    final File file = new File(sdCardPath);
                    file.delete();
                    subscriber.onNext(filePath);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private static String unTarFileAndExtract(final String sdCardPath, final String desiredFileName) throws Exception {
        FileInputStream fin = null;
        BufferedInputStream in = null;
        GzipCompressorInputStream gzIn = null;
        TarArchiveInputStream tarIn = null;

        FileOutputStream fos = null;
        BufferedOutputStream dest = null;
        try {
            fin = new FileInputStream(sdCardPath);
            in = new BufferedInputStream(fin);
            gzIn = new GzipCompressorInputStream(in);
            tarIn = new TarArchiveInputStream(gzIn);

            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
                final String name = entry.getName();
                final String onlyExtractedFileName = name.substring(name.lastIndexOf(File.separator) + 1);
                if (onlyExtractedFileName.equals(desiredFileName)) {
                    fos = new FileOutputStream(SD_CARD_TRAINED_DATA_PATH + onlyExtractedFileName);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    int count;
                    byte data[] = new byte[BUFFER];
                    while ((count = tarIn.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    return SD_CARD_TRAINED_DATA_PATH + onlyExtractedFileName;
                }
            }
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (in != null) {
                in.close();
            }
            if (gzIn != null) {
                gzIn.close();
            }
            if (tarIn != null) {
                tarIn.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (dest != null) {
                dest.close();
            }
        }
        return null;
    }
}
