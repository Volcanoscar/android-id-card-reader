package com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.retrofit;

import com.eftimoff.idcardreader.components.tesseract.listeners.GzipFileDownloadListener;
import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.TrainedDataDownloader;

import java.io.IOException;
import java.io.InputStream;

import retrofit.RestAdapter;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RetrofitTrainedDataDownloader implements TrainedDataDownloader {

    private static final String ENDPOINT = "https://tesseract-ocr.googlecode.com";
    private static final String FILE_NAME = "tesseract-ocr-3.02.%s.tar.gz";

    private final RetrofitTrainedDataDownloaderInterface retrofitTrainedDataDownloaderInterface;

    private RetrofitTrainedDataDownloader() {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();
        retrofitTrainedDataDownloaderInterface = restAdapter.create(RetrofitTrainedDataDownloaderInterface.class);

    }

    @Override
    public void downloadTrainedFile(final String language, final GzipFileDownloadListener listener) {
        final String fileName = String.format(FILE_NAME, language);
        if (listener != null) {
            listener.onStart();
        }
        final Observable<String> observable = retrofitTrainedDataDownloaderInterface.downloadGzipFile(fileName)
                .flatMap(new Func1<Response, Observable<String>>() {
                    @Override
                    public Observable<String> call(final Response response) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(final Subscriber<? super String> subscriber) {
                                try {
                                    final String downloadFileToSdCard = downloadFileToSdCard(response);
                                    subscriber.onNext(downloadFileToSdCard);
                                    subscriber.onCompleted();
                                } catch (IOException e) {
                                    subscriber.onError(e);
                                }
                            }
                        });
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

    private String downloadFileToSdCard(final Response response) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = response.getBody().in();
            //TODO finish downloading the file and test the who thing.
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return null;
    }
}
