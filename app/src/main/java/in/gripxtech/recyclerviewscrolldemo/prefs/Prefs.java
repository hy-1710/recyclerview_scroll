package in.gripxtech.recyclerviewscrolldemo.prefs;

import android.content.Context;
import android.support.annotation.NonNull;

public class Prefs extends BasePrefs {

    public static final String TAG;

    private static final String ScrollX;
    private static final String ScrollY;

    static {
        TAG = "Prefs";
        ScrollX = "ScrollX";
        ScrollY = "ScrollY";
    }

    /**
     * @param context context
     */
    public Prefs(@NonNull Context context) {
        super(TAG, context);
    }

    public int getScrollX() {
        return getInt(ScrollX);
    }

    public void setScrollX(int scrollX) {
        putInt(ScrollX, scrollX);
    }

    public int getScrollY() {
        return getInt(ScrollY);
    }

    public void setScrollY(int scrollY) {
        putInt(ScrollY, scrollY);
    }
}
