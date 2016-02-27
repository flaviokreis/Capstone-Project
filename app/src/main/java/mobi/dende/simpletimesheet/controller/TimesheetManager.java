package mobi.dende.simpletimesheet.controller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobi.dende.simpletimesheet.data.TimesheetContact;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.Task;
import mobi.dende.simpletimesheet.model.Timer;

/**
 * Manager Projects, Tasks and Timers.
 */
public class TimesheetManager {

    public static List<Project> getProjects(Context context){
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

    public static Project getProjectById(Context context, long id){
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

    public static boolean addProject(Context context, Project project){
        if(project == null){
            return false;
        }

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

            return updateResult > 0;
        }
        else{
            values.put(TimesheetContact.Projects.COLUMN_UPDATED_DATE, project.getInsertedDate().getTime());

            Uri result = contentResolver.insert(TimesheetContact.Projects.CONTENT_URI, values);

            return result != null;
        }
    }

    public static void removeProject(Context context, Project project){
        if( (project == null) || (project.getId() <= 0)){
            return ;
        }

        Uri removeUri = TimesheetContact.Projects.buildtUri(project.getId());

        ContentResolver contentResolver = context.getContentResolver();
        int result = contentResolver.delete(removeUri, null, null);
    }

    public static List<Task> getTasks(Context context, List<Project> projects){
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

    public static Task getTaskById(Context context, long id){
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

    public static boolean addTask(Context context, Task task){
        if(task == null){
            return false;
        }

        ContentResolver contentResolver = context.getContentResolver();

        Uri result = null;

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

            result = contentResolver.insert(TimesheetContact.Tasks.CONTENT_URI, values);

            return result != null;
        }
    }

    public static long addTimer(Context context, Timer timer){
        if(timer == null){
            return 0;
        }

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

    public static Timer getPlayedTimer(Context context){
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
}
