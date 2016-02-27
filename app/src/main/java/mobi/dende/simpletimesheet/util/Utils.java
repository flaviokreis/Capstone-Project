package mobi.dende.simpletimesheet.util;

import android.content.Context;
import android.content.res.Configuration;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;

/**
 * Created by flaviokreis on 21/02/16.
 */
public class Utils {

    public static String[] months = new String[] {
            "J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    public static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static final int getProjectTheme(Context context, Project project){
        if(project != null){
            if(project.getColor() == context.getResources().getColor(R.color.project_color_1)){
                return R.style.Projects_Theme1;
            }
            else if(project.getColor() == context.getResources().getColor(R.color.project_color_2)){
                return R.style.Projects_Theme2;
            }
            else if(project.getColor() == context.getResources().getColor(R.color.project_color_3)){
                return R.style.Projects_Theme3;
            }
            else if(project.getColor() == context.getResources().getColor(R.color.project_color_4)){
                return R.style.Projects_Theme4;
            }
            else if(project.getColor() == context.getResources().getColor(R.color.project_color_5)){
                return R.style.Projects_Theme5;
            }
            else if(project.getColor() == context.getResources().getColor(R.color.project_color_6)){
                return R.style.Projects_Theme6;
            }
            else if(project.getColor() == context.getResources().getColor(R.color.project_color_7)){
                return R.style.Projects_Theme7;
            }
            else if(project.getColor() == context.getResources().getColor(R.color.project_color_8)){
                return R.style.Projects_Theme8;
            }
        }

        return R.style.AppTheme_NoActionBar;
    }
}
