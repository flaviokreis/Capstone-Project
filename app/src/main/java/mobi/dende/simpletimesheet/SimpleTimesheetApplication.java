package mobi.dende.simpletimesheet;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Custom application to start frameworks
 */
public class SimpleTimesheetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
