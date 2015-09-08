package com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.retrofit;

import com.eftimoff.idcardreader.components.tesseract.listeners.GzipFileDownloadListener;
import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.TrainedDataDownloader;
import com.eftimoff.idcardreader.utils.FileUtils;
import com.google.common.base.Preconditions;

import retrofit.RestAdapter;
import retrofit.client.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RetrofitTrainedDataDownloader implements TrainedDataDownloader {

    private static final String ENDPOINT = "https://tesseract-ocr.googlecode.com";
    private static final String HOST_FILE_NAME = "tesseract-ocr-3.02.%s.tar.gz";
    private static final String SD_CARD_FILE_NAME = "%s.traineddata";

    private static RetrofitTrainedDataDownloader instance;

    private final RetrofitTrainedDataDownloaderInterface retrofitTrainedDataDownloaderInterface;

    public static RetrofitTrainedDataDownloader getInstance() {
        if (instance == null) {
            synchronized (RetrofitTrainedDataDownloader.class) {
                if (instance == null) {
                    instance = new RetrofitTrainedDataDownloader();
                }
            }
        }
        return instance;
    }

    private RetrofitTrainedDataDownloader() {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        retrofitTrainedDataDownloaderInterface = restAdapter.create(RetrofitTrainedDataDownloaderInterface.class);
    }

    @Override
    public void downloadTrainedFile(final String language, final GzipFileDownloadListener listener) {
        Preconditions.checkNotNull(language, "Language must not be null.");
        final String fileName = String.format(HOST_FILE_NAME, language);
        if (listener != null) {
            listener.onStart();
        }
        final Observable<String> observable = retrofitTrainedDataDownloaderInterface.downloadGzipFile(fileName)
                .flatMap(new Func1<Response, Observable<String>>() {
                    @Override
                    public Observable<String> call(final Response response) {
                        return fromResponseToSdCard(response, language);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String sdCardPath) {
                        final String desiredFileName = String.format(SD_CARD_FILE_NAME, language);
                        return unTarFile(sdCardPath, desiredFileName);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Action1<String>() {
            @Override
            public void call(final String sdCardFilePath) {
                if (listener != null) {
                    listener.onDone(sdCardFilePath);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(final Throwable throwable) {
                if (listener != null) {
                    listener.onError(throwable);
                }
            }
        });
    }

    private Observable<String> unTarFile(final String sdCardPath, final String desiredFileName) {
        Preconditions.checkNotNull(sdCardPath, "Sd card path must not be null.");
        return FileUtils.unTarFileExtractAndDeleteTar(sdCardPath, desiredFileName);
    }

    private Observable<String> fromResponseToSdCard(final Response response, final String language) {
        Preconditions.checkNotNull(response, "Response must not be null.");
        final String fileName = String.format(HOST_FILE_NAME, language);
        return FileUtils.downloadFileToSdCard(response, fileName);
    }
}
