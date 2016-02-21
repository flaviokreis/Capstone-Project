package mobi.dende.simpletimesheet.controller;

import android.content.ContentResolver;
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

    public static boolean addProject(Context context, Project project){
        if(project == null){
            return false;
        }

        ContentResolver contentResolver = context.getContentResolver();

        Uri result = null;

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

            result = contentResolver.insert(TimesheetContact.Projects.CONTENT_URI, values);

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
}
