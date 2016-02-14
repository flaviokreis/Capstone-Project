package mobi.dende.simpletimesheet.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class TimesheetProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TimesheetDbHelper mOpenHelper;

    static final int PROJECT        = 100;
    static final int PROJECTS       = 101;

    static final int TASK           = 200;
    static final int TASKS          = 201;

    static final int TIMER                  = 300;
    static final int TIMERS                 = 301;
    static final int TIMER_PROJECT_AND_DATE = 303;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TimesheetContact.CONTENT_AUTHORITY;

        matcher.addURI(authority, TimesheetContact.PATH_PROJECT + "/#", PROJECT);
        matcher.addURI(authority, TimesheetContact.PATH_PROJECT, PROJECTS);

        matcher.addURI(authority, TimesheetContact.PATH_TASK + "/#", TASK);
        matcher.addURI(authority, TimesheetContact.PATH_TASK, TASKS);

        matcher.addURI(authority, TimesheetContact.PATH_TIMER + "/#", TIMER);
        matcher.addURI(authority, TimesheetContact.PATH_TIMER, TIMERS);
        matcher.addURI(authority, TimesheetContact.PATH_TIMER + "/#/#/#", TIMER_PROJECT_AND_DATE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TimesheetDbHelper(getContext());
        mOpenHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case PROJECT:
                return TimesheetContact.Projects.CONTENT_ITEM_TYPE;
            case PROJECTS:
                return TimesheetContact.Projects.CONTENT_TYPE;
            case TASK:
                return TimesheetContact.Tasks.CONTENT_ITEM_TYPE;
            case TASKS:
                return TimesheetContact.Tasks.CONTENT_TYPE;
            case TIMER:
                return TimesheetContact.Timers.CONTENT_ITEM_TYPE;
            case TIMERS:
                return TimesheetContact.Timers.CONTENT_TYPE;
            case TIMER_PROJECT_AND_DATE:
                return TimesheetContact.Timers.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case PROJECT:
                if( uri.getPathSegments().size() > 1 ){
                    sqlBuilder.appendWhere(TimesheetContact.Projects._ID + " = " + uri.getPathSegments().get(1));
                }
            case PROJECTS:
                sqlBuilder.setTables(TimesheetContact.Projects.TABLE_NAME);
                cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TASK:
                if( uri.getPathSegments().size() > 1 ){
                    sqlBuilder.appendWhere(TimesheetContact.Tasks._ID + " = " + uri.getPathSegments().get(1));
                }
            case TASKS:
                sqlBuilder.setTables(TimesheetContact.Tasks.TABLE_NAME);
                cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TIMER:
                if( uri.getPathSegments().size() > 1 ){
                    sqlBuilder.appendWhere(TimesheetContact.Timers._ID + " = " + uri.getPathSegments().get(1));
                }
            case TIMERS:
                sqlBuilder.setTables(TimesheetContact.Timers.TABLE_NAME);
                cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TIMER_PROJECT_AND_DATE:
                if( uri.getPathSegments().size() > 3 ){
                    sqlBuilder.appendWhere(TimesheetContact.Timers.COLUMN_PROJECT_ID + " = " + uri.getPathSegments().get(1));
                    sqlBuilder.appendWhere(TimesheetContact.Timers.COLUMN_START_DATE + " = " + uri.getPathSegments().get(2));
                    sqlBuilder.appendWhere(TimesheetContact.Timers.COLUMN_END_DATE + " = " + uri.getPathSegments().get(3));
                }
                sqlBuilder.setTables(TimesheetContact.Timers.TABLE_NAME);
                cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName;

        switch (sUriMatcher.match(uri)){
            case PROJECT:
            case PROJECTS:
                tableName = TimesheetContact.Projects.TABLE_NAME;
                break;
            case TASK:
            case TASKS:
                tableName = TimesheetContact.Tasks.TABLE_NAME;
                break;
            case TIMER:
            case TIMERS:
            case TIMER_PROJECT_AND_DATE:
                tableName = TimesheetContact.Timers.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long rowId = db.insert(tableName, "", values);
        if ( rowId > 0 ) {
            Uri newUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange( newUri, null );
            return newUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName;

        switch (sUriMatcher.match(uri)){
            case PROJECT:
            case PROJECTS:
                tableName = TimesheetContact.Projects.TABLE_NAME;
                break;
            case TASK:
            case TASKS:
                tableName = TimesheetContact.Tasks.TABLE_NAME;
                break;
            case TIMER:
            case TIMERS:
            case TIMER_PROJECT_AND_DATE:
                tableName = TimesheetContact.Timers.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int rowsDeleted = db.delete(tableName, selection, selectionArgs);

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String tableName;
        switch (sUriMatcher.match(uri)){
            case PROJECT:
            case PROJECTS:
                tableName = TimesheetContact.Projects.TABLE_NAME;
                break;
            case TASK:
            case TASKS:
                tableName = TimesheetContact.Tasks.TABLE_NAME;
                break;
            case TIMER:
            case TIMERS:
            case TIMER_PROJECT_AND_DATE:
                tableName = TimesheetContact.Timers.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int rowsUpdated = db.update(tableName, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
