package com.eftimoff.idcardreader.components.tesseract.images;

import android.graphics.Bitmap;
import android.graphics.Rect;

public interface TesseractBitmapConverter {

    Bitmap convertToBitmap(final byte[] array, final int width, final int height, final Rect rect);
}
