package com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.retrofit;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface RetrofitTrainedDataDownloaderInterface {

    //https://tesseract-ocr.googlecode.com/files/tesseract-ocr-3.02.eng.tar.gz

    @GET("/files/{file}")
    Observable<Response> downloadGzipFile(@Path("file") String file);
}
