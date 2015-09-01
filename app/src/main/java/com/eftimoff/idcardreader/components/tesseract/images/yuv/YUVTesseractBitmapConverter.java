package com.eftimoff.idcardreader.components.tesseract.images.yuv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import com.eftimoff.idcardreader.components.tesseract.images.TesseractBitmapConverter;

import java.io.ByteArrayOutputStream;

public class YUVTesseractBitmapConverter implements TesseractBitmapConverter {

    private static final int JPEG_QUALITY = 60;

    @Override
    public Bitmap convertToBitmap(final byte[] array, final int width, final int height, final Rect rect) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final YuvImage image = new YuvImage(array, ImageFormat.NV21, rect.width(), rect.height(), null);
        image.compressToJpeg(rect, JPEG_QUALITY, byteArrayOutputStream);
        return BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
    }
}
