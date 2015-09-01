package com.eftimoff.idcardreader.components.tesseract.images.planar;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.eftimoff.idcardreader.components.tesseract.images.TesseractBitmapConverter;

public class PlanarYUVTesseractBitmapConverter implements TesseractBitmapConverter {

    @Override
    public Bitmap convertToBitmap(final byte[] array, final int width, final int height, final Rect rect) {
        final PlanarYUVLuminanceSource planarYUVLuminanceSource = new PlanarYUVLuminanceSource(array, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
        return planarYUVLuminanceSource.renderCroppedGreyscaleBitmap();
    }
}
