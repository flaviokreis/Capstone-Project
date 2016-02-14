package mobi.dende.simpletimesheet.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mobi.dende.simpletimesheet.data.TimesheetContact.Projects;
import mobi.dende.simpletimesheet.data.TimesheetContact.Tasks;
import mobi.dende.simpletimesheet.data.TimesheetContact.Timers;

/**
 * Manages a local database for timesheet data.
 */
public class TimesheetDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "timesheet.db";

    final String SQL_CREATE_PROJECT_TABLE = "CREATE TABLE " + Projects.TABLE_NAME + " (" +
            Projects._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Projects.COLUMN_NAME + " TEXT NOT NULL, " +
            Projects.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            Projects.COLUMN_VALUE_HOUR + " REAL NOT NULL DEFAULT 0, " +
            Projects.COLUMN_COLOR + " INTEGER NOT NULL DEFAULT 0, " +
            Projects.COLUMN_INSERTED_DATE + " INTEGER NOT NULL, " +
            Projects.COLUMN_UPDATED_DATE + " INTEGER );";

    final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + Tasks.TABLE_NAME + " (" +
            Tasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Tasks.COLUMN_PROJECT_ID + " INTEGER NOT NULL, " +
            Tasks.COLUMN_NAME + " TEXT NOT NULL, " +
            Tasks.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
            Tasks.COLUMN_COLOR + " INTEGER NOT NULL DEFAULT 0, " +
            Tasks.COLUMN_INSERTED_DATE + " INTEGER NOT NULL, " +
            Tasks.COLUMN_UPDATED_DATE + " INTEGER " +
            " );";

    final String SQL_CREATE_TIMER_TABLE = "CREATE TABLE " + Timers.TABLE_NAME + " (" +
            Timers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Timers.COLUMN_PROJECT_ID + " INTEGER NOT NULL, " +
            Timers.COLUMN_TASK_ID + " INTEGER NOT NULL, " +
            Timers.COLUMN_START_DATE + " INTEGER NOT NULL, " +
            Timers.COLUMN_END_DATE + " INTEGER " +
            " );";

    public TimesheetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PROJECT_TABLE);
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_TIMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //NO CODE
    }
}
