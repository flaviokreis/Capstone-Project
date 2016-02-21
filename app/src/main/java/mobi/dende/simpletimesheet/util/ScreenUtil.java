package mobi.dende.simpletimesheet.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by flaviokreis on 21/02/16.
 */
public class ScreenUtil {

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    public static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}
