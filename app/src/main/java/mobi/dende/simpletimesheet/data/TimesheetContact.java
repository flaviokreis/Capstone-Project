package mobi.dende.simpletimesheet.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the timesheet database.
 */
public class TimesheetContact {

    public static final String CONTENT_AUTHORITY = "dende.mobi.simpletimesheet";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PROJECT = "project";
    public static final String PATH_TASK    = "task";
    public static final String PATH_TIMER   = "timer";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the project table */
    public static final class Projects implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROJECT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT;

        public static final String TABLE_NAME = "project";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_VALUE_HOUR = "value_hour";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_COLOR = "color";

        public static final String COLUMN_INSERTED_DATE = "inserted_date";

        public static final String COLUMN_UPDATED_DATE = "updated_date";

        public static Uri buildtUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the task table */
    public static final class Tasks implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        public static final String TABLE_NAME = "task";

        public static final String COLUMN_PROJECT_ID = "project_id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_COLOR = "color";

        public static final String COLUMN_INSERTED_DATE = "inserted_date";

        public static final String COLUMN_UPDATED_DATE = "updated_date";

        public static Uri buildtUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the timer table */
    public static final class Timers implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TIMER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIMER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIMER;

        public static final String TABLE_NAME = "timer";

        public static final String COLUMN_PROJECT_ID = "project_id";

        public static final String COLUMN_TASK_ID = "task_id";

        public static final String COLUMN_START_DATE = "start_time";

        public static final String COLUMN_END_DATE = "end_time";

        public static Uri buildtUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
