package com.eftimoff.idcardreader.components.passport.service.assets.reader.assets;

import android.content.Context;
import android.content.res.AssetManager;

import com.eftimoff.idcardreader.components.passport.service.assets.reader.FileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AssetFileReader implements FileReader {

    private final AssetManager assetsManager;

    public AssetFileReader(final Context context) {
        assetsManager = context.getAssets();
    }

    @Override
    public Observable<String> readFromFile(final String fileName) {
        final Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                if (fileName == null) {
                    subscriber.onError(new IllegalArgumentException("Filename must not be null."));
                    return;
                }
                final StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(assetsManager.open(fileName)));
                    String line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line);
                        line = reader.readLine();
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                }
                subscriber.onNext(stringBuilder.toString());
                subscriber.onCompleted();
            }
        });
        stringObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        return stringObservable;
    }
}
