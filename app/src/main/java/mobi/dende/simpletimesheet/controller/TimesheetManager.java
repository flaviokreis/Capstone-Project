package mobi.dende.simpletimesheet.controller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobi.dende.simpletimesheet.data.TimesheetContact;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.ProjectDetail;
import mobi.dende.simpletimesheet.model.ProjectExport;
import mobi.dende.simpletimesheet.model.Task;
import mobi.dende.simpletimesheet.model.Timer;

/**
 * Manager Projects, Tasks and Timers.
 */
public class TimesheetManager {

    @NonNull
    public static List<Project> getProjects(@NonNull Context context){
        ContentResolver contentResolver = context.getContentResolver();

        List<Project> list = new ArrayList<>();

        final Cursor cursor = contentResolver.query(TimesheetContact.Projects.CONTENT_URI,
                TimesheetContact.Projects.PROJECTION,
                null,
                null,
                TimesheetContact.Projects.COLUMN_INSERTED_DATE + " DESC ");


        if ( cursor != null ) {
            if ( cursor.moveToFirst() ) {
                Project project;
                do{
                    project = new Project();
                    project.setId(cursor.getLong(0));
                    project.setName(cursor.getString(1));
                    project.setValueHour(cursor.getFloat(2));
                    project.setDescription(cursor.getString(3));
                    project.setColor(cursor.getInt(4));
                    project.setInsertedDate(new Date(cursor.getLong(5)));
                    project.setUpdatedDate(new Date(cursor.getLong(6)));

                    list.add( project );
                }
                while ( cursor.moveToNext() ) ;

            }
            cursor.close();
        }

        return list;
    }

    @Nullable
    public static Project getProjectById(@NonNull Context context, long id){
        ContentResolver contentResolver = context.getContentResolver();

        final Cursor cursor = contentResolver.query(TimesheetContact.Projects.CONTENT_URI,
                TimesheetContact.Projects.PROJECTION,
                TimesheetContact.Projects._ID + " = ? ",
                new String[]{String.valueOf(id)},
                null);


        if ( cursor != null ) {
            Project project = null;
            if ( cursor.moveToFirst() ) {
                project = new Project();
                project.setId(cursor.getLong(0));
                project.setName(cursor.getString(1));
                project.setValueHour(cursor.getFloat(2));
                project.setDescription(cursor.getString(3));
                project.setColor(cursor.getInt(4));
                project.setInsertedDate(new Date(cursor.getLong(5)));
                project.setUpdatedDate(new Date(cursor.getLong(6)));
            }

            cursor.close();

            return project;
        }

        return null;
    }

    public static boolean addProject(@NonNull Context context, @NonNull Project project){
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(TimesheetContact.Projects.COLUMN_NAME, project.getName());
        values.put(TimesheetContact.Projects.COLUMN_VALUE_HOUR, project.getValueHour());
        values.put(TimesheetContact.Projects.COLUMN_DESCRIPTION, project.getDescription());
        values.put(TimesheetContact.Projects.COLUMN_COLOR, project.getColor());
        values.put(TimesheetContact.Projects.COLUMN_INSERTED_DATE, project.getInsertedDate().getTime());
        if(project.getId() > 0){
            values.put(TimesheetContact.Projects.COLUMN_UPDATED_DATE, new Date().getTime());

            int updateResult = contentResolver.update(TimesheetContact.Projects.CONTENT_URI,
                    values,
                    TimesheetContact.Projects._ID + " ? ",
                    new String[]{String.valueOf(project.getId())});

            updateWidgets(context);

            return updateResult > 0;
        }
        else{
            values.put(TimesheetContact.Projects.COLUMN_UPDATED_DATE, project.getInsertedDate().getTime());

            Uri result = contentResolver.insert(TimesheetContact.Projects.CONTENT_URI, values);

            updateWidgets(context);

            return result != null;
        }
    }

    private static void updateWidgets(Context context) {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(TimesheetContact.Projects.ACTION_PROJECT_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    public static void removeProject(@NonNull Context context, @NonNull Project project){
        if( (project == null) || (project.getId() <= 0)){
            return ;
        }

        Uri removeUri = TimesheetContact.Projects.buildtUri(project.getId());

        ContentResolver contentResolver = context.getContentResolver();
        int result = contentResolver.delete(removeUri, null, null);
    }

    @Nullable
    public static List<Task> getTasks(@NonNull Context context, @NonNull List<Project> projects){
        ContentResolver contentResolver = context.getContentResolver();

        List<Task> list = new ArrayList<>();

        final Cursor cursor = contentResolver.query(TimesheetContact.Tasks.CONTENT_URI,
                TimesheetContact.Tasks.PROJECTION,
                null,
                null,
                TimesheetContact.Projects.COLUMN_INSERTED_DATE + " DESC ");


        if ( cursor != null ) {
            if ( cursor.moveToFirst() ) {
                Task task;
                do{

                    task = new Task();
                    task.setId(cursor.getLong(0));
                    Project project1 = null;
                    for(Project project : projects){
                        if(project.getId() == cursor.getLong(1)){
                            project1 = project;
                            break;
                        }
                    }
                    task.setProject(project1);
                    task.setName(cursor.getString(2));
                    task.setDescription(cursor.getString(3));
                    task.setColor(cursor.getInt(4));
                    task.setInsertedDate(new Date(cursor.getLong(5)));
                    task.setUpdatedDate(new Date(cursor.getLong(6)));

                    list.add( task );
                }
                while ( cursor.moveToNext() ) ;

            }
            cursor.close();
        }

        return list;
    }

    @Nullable
    public static Task getTaskById(@NonNull Context context, long id){
        ContentResolver contentResolver = context.getContentResolver();

        final Cursor cursor = contentResolver.query(TimesheetContact.Tasks.CONTENT_URI,
                TimesheetContact.Tasks.PROJECTION,
                TimesheetContact.Tasks._ID + " = ? ",
                new String[]{String.valueOf(id)},
                null);


        if ( cursor != null ) {
            Task task = null;
            if ( cursor.moveToFirst() ) {
                task = new Task();
                task.setId(cursor.getLong(0));
                task.setName(cursor.getString(2));
                task.setDescription(cursor.getString(3));
                task.setColor(cursor.getInt(4));
                task.setInsertedDate(new Date(cursor.getLong(5)));
                task.setUpdatedDate(new Date(cursor.getLong(6)));
            }

            cursor.close();

            return task;
        }

        return null;
    }

    public static boolean addTask(@NonNull Context context, @NonNull Task task){
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(TimesheetContact.Tasks.COLUMN_PROJECT_ID, task.getProject().getId());
        values.put(TimesheetContact.Tasks.COLUMN_NAME, task.getName());
        values.put(TimesheetContact.Tasks.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TimesheetContact.Tasks.COLUMN_COLOR, task.getColor());
        values.put(TimesheetContact.Tasks.COLUMN_INSERTED_DATE, task.getInsertedDate().getTime());
        if(task.getId() > 0){
            values.put(TimesheetContact.Tasks.COLUMN_UPDATED_DATE, new Date().getTime());

            int updateResult = contentResolver.update(TimesheetContact.Tasks.CONTENT_URI,
                    values,
                    TimesheetContact.Projects._ID + " ? ",
                    new String[]{String.valueOf(task.getId())});

            return updateResult > 0;
        }
        else{
            values.put(TimesheetContact.Projects.COLUMN_UPDATED_DATE, task.getInsertedDate().getTime());

            Uri result = contentResolver.insert(TimesheetContact.Tasks.CONTENT_URI, values);

            return result != null;
        }
    }

    @Nullable
    public static long addTimer(@NonNull Context context, @NonNull Timer timer){
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(TimesheetContact.Timers.COLUMN_TASK_ID, timer.getTask().getId());
        values.put(TimesheetContact.Timers.COLUMN_PROJECT_ID, timer.getTask().getProject().getId());
        values.put(TimesheetContact.Timers.COLUMN_START_DATE, timer.getStartTime().getTime());
        if(timer.getEndTime() != null){
            values.put(TimesheetContact.Timers.COLUMN_END_DATE, timer.getEndTime().getTime());
        }

        if(timer.getId() > 0){
            int updateResult = contentResolver.update(TimesheetContact.Timers.CONTENT_URI,
                    values,
                    TimesheetContact.Timers._ID + " = ? ",
                    new String[]{String.valueOf(timer.getId())});

            return timer.getId();
        }
        else{
            Uri result = contentResolver.insert(TimesheetContact.Timers.CONTENT_URI, values);

            return ContentUris.parseId(result);
        }
    }

    @Nullable
    public static Timer getPlayedTimer(@NonNull Context context){
        final Cursor cursor = context.getContentResolver().query(
                TimesheetContact.Timers.CONTENT_URI,
                TimesheetContact.Timers.PROJECTION,
                TimesheetContact.Timers.COLUMN_END_DATE + " IS NULL ",
                null,
                null);

        if((cursor != null) && cursor.moveToFirst()){
            Project project = getProjectById(context, cursor.getLong(1));
            Task task = getTaskById(context, cursor.getLong(2));
            if(task != null){
                task.setProject(project);
            }

            Timer timer = new Timer();
            timer.setId(cursor.getLong(0));
            timer.setStartTime(new Date(cursor.getLong(3)));
            timer.setTask(task);

            cursor.close();

            return timer;
        }

        return null;
    }

    @Nullable
    public static ProjectDetail getProjectDetail(@NonNull Context context, @NonNull Project project){
        final Cursor cursor = context.getContentResolver().query(
                TimesheetContact.Timers.CONTENT_URI,
                TimesheetContact.Timers.PROJECTION,
                TimesheetContact.Timers.COLUMN_PROJECT_ID + " = ? ",
                new String[]{String.valueOf(project.getId())},
                TimesheetContact.Timers.COLUMN_START_DATE + " ASC, " +
                TimesheetContact.Timers._ID + " ASC ");

        ProjectDetail detail = new ProjectDetail(project);
        detail.setEarnPerHour( project.getValueHour() );

        List<Integer> hours = new ArrayList<>();

        if((cursor != null) && cursor.moveToFirst()){
            //TODO pegar primeiro e ultimo dia do ano
            Calendar calendar = Calendar.getInstance();

            int actualDay   = calendar.get(Calendar.DAY_OF_MONTH);
            int actualWeek  = calendar.get(Calendar.WEEK_OF_MONTH);
            int actualMonth = calendar.get(Calendar.MONTH);
            int actualYear  = calendar.get(Calendar.YEAR);

            Map<Integer, Integer> months = new HashMap<>();
            months.put(Calendar.JANUARY,    0);
            months.put(Calendar.FEBRUARY,   0);
            months.put(Calendar.MARCH,      0);
            months.put(Calendar.APRIL,      0);
            months.put(Calendar.MAY,        0);
            months.put(Calendar.JUNE,       0);
            months.put(Calendar.JULY,       0);
            months.put(Calendar.AUGUST,     0);
            months.put(Calendar.SEPTEMBER,  0);
            months.put(Calendar.OCTOBER,    0);
            months.put(Calendar.NOVEMBER,   0);
            months.put(Calendar.DECEMBER,   0);

            long totalTime = 0;
            long todayTime = 0;
            long weekTime  = 0;
            long monthTime = 0;
            do{
                long start  = cursor.getLong(3);
                long end    = cursor.getLong(4);
                long diffInSeconds = (end - start) / 1000;

                calendar.setTimeInMillis(start);

                if(calendar.get(Calendar.YEAR) == actualYear ){
                    int monthSum = months.get(calendar.get(Calendar.MONTH));
                    monthSum += (diffInSeconds / 60);

                    months.put(calendar.get(Calendar.MONTH), monthSum);

                    if( calendar.get(Calendar.MONTH) == actualMonth ){ //This month
                        monthTime += (diffInSeconds / 60);
                        if( calendar.get(Calendar.WEEK_OF_MONTH) == actualWeek ){ //This week
                            weekTime += (diffInSeconds / 60);
                            if( calendar.get(Calendar.DAY_OF_MONTH) == actualDay ){ //Today
                                todayTime += (diffInSeconds / 60);
                            }
                        }
                    }
                }

                totalTime += diffInSeconds;
            }
            while (cursor.moveToNext());

            cursor.close();

            detail.setEarnMonth(project.getValueHour() * (monthTime / 60));
            detail.setEarnTotal(project.getValueHour() * (totalTime / 3600));

            detail.setMinutesToday(todayTime);
            detail.setMinutesWeek(weekTime);
            detail.setMinutesMonth(monthTime);

            for(int i = 0; i < months.size(); i++){
                int value = ( months.get(i) / 60 );
                hours.add(value);
            }
        }

        detail.setMonths(hours);

        return detail;
    }

    public static ProjectExport getProjectExportDetail(Context context, ProjectExport projectExport){
        if(projectExport != null){
            Uri uri = TimesheetContact.Timers.buildProjectAndDateUri(
                    projectExport.getProject().getId(),
                    projectExport.getStartDate().getTime(),
                    projectExport.getEndDate().getTime() );

            Log.d("TimesheetManager", "Uri: " + uri.toString());

            final Cursor cursor = context.getContentResolver().query(
                    uri,
                    TimesheetContact.Timers.PROJECTION,
                    null,
                    null,
                    null);

            if((cursor != null) && cursor.moveToFirst()){
                Log.d("TimesheetManager", "cursor size: " + cursor.getCount());
                List<Timer> list = new ArrayList<>();
                Map<Long, Task> tasks = new HashMap<>();
                Project project = getProjectById(context, cursor.getLong(1));
                Task task;
                Timer timer;
                long totalMinutes = 0;
                do{
                    long start = cursor.getLong(3);
                    long end = cursor.getLong(4);
                    totalMinutes += ((end-start) / 60000);
                    task = tasks.get(cursor.getLong(2));
                    if(task == null){
                        task = getTaskById(context, cursor.getLong(2));
                        if(task != null){
                            tasks.put(task.getId(), task);
                            task.setProject(project);
                        }
                    }

                    timer = new Timer();
                    timer.setId(cursor.getLong(0));
                    timer.setStartTime(new Date(start));
                    timer.setEndTime(new Date(end));
                    timer.setTask(task);

                    list.add(timer);
                }
                while(cursor.moveToNext());

                Log.d("TimesheetManager", "Total count size: " + totalMinutes);

                cursor.close();

                projectExport.setTimers(list);
                projectExport.setTotalHours((int)(totalMinutes / 60));
            }
            else {
                Log.d("TimesheetManager", "Cursor is null ou empty.");
            }

        }

        return projectExport;
    }
}
