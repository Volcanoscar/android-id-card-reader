package com.eftimoff.idcardreader.components.passport.area;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class AreaHelper {

    private Context context;

    public AreaHelper(final Context context) {
        this.context = context;
    }

    public Point getDisplayDimensions() {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        size.set(size.x, size.y - getStatusBarHeight());
        return size;
    }

    public Rect getDefaultIdCardSize() {
        final Point displayDimensions = getDisplayDimensions();
        final int width = displayDimensions.x;
        final int height = displayDimensions.y;
        if (width > height) {
            //landscape
            final int offsetWidth = (int) (width * 0.2);
            final int idCardWidth = (int) (width * 0.6);
            final int idCardHeight = (int) (idCardWidth / 1.58);
            final int topIdCardHeight = idCardWidth / 2 - idCardHeight / 2;
            final int bottomIdCardHeight = idCardWidth / 2 + idCardHeight / 2;
            return new Rect(offsetWidth, topIdCardHeight, width - offsetWidth, bottomIdCardHeight);
        } else {
            //portrait
            final int offsetWidth = (int) (width * 0.2);
            final int idCardWidth = (int) (width * 0.6);
            final int idCardHeight = (int) (idCardWidth / 1.58);
            final int topIdCardHeight = idCardWidth / 2 - idCardHeight / 2;
            final int bottomIdCardHeight = idCardWidth / 2 + idCardHeight / 2;
            return new Rect(offsetWidth, topIdCardHeight, width - offsetWidth, bottomIdCardHeight);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
