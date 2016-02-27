package mobi.dende.simpletimesheet.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.controller.TimesheetManager;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.Task;
import mobi.dende.simpletimesheet.ui.fragment.ProjectDetailsFragment;
import mobi.dende.simpletimesheet.ui.fragment.ProjectFragment;

public class MainActivity extends AppCompatActivity implements OnProjectScreenListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_TASK = "task";

    private ProjectFragment mProjectFragment;

    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    private CollapsingToolbarLayout mCollapsingBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView mProjectName;
    private TextView mTaskName;
    private TextView mDuration;

    private mobi.dende.simpletimesheet.model.Timer mPlayedTimer;

    private final Handler handler = new Handler();
    private Timer timer = new Timer();

    TimerTask doAsynchronousTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(MainActivity.this, R.xml.pref_general, false);

        if(savedInstanceState == null){
            mProjectFragment = new ProjectFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, mProjectFragment)
                    .commit();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mCollapsingBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mProjectName = (TextView) findViewById(R.id.toolbar_project_name);
        mTaskName    = (TextView) findViewById(R.id.toolbar_task_name);
        mDuration    = (TextView) findViewById(R.id.toolbar_task_duration);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new CreateProjectDialog();
                ((CreateProjectDialog)dialog).setListener(new CreateProjectDialog.OnCreateProjectListener() {
                    @Override
                    public void onCreateProject(Project project) {
                        new SaveProjectAsync().execute(project);
                    }
                });
                dialog.show(getSupportFragmentManager(), "project_dialog");
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_navigation_view);

        mDrawerToggle = setupDrawerToggle();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();

                        boolean isItemSelected = false;

                        switch (menuItem.getItemId()) {
                            case R.id.nav_dashboard:
                                isItemSelected = true;
                                break;
                            case R.id.nav_export:
                                Intent intentExport = new Intent(MainActivity.this, ExportActivity.class);
                                startActivity(intentExport);
                                break;
                            case R.id.nav_settings:
                                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intentSettings);
                                break;
                            case R.id.nav_support:
                                Intent intentSupport = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                                intentSupport.setType("message/rfc822");
                                intentSupport.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"abc@xyz.com"});
                                intentSupport.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject));
                                Intent mailer = Intent.createChooser(intentSupport, null);
                                startActivity(mailer);
                                break;
                        }

                        return isItemSelected;
                    }
                });

        if(savedInstanceState != null){
            mPlayedTimer = savedInstanceState.getParcelable(KEY_TASK);
            showTask();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_TASK, mPlayedTimer);
        super.onSaveInstanceState(outState);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if(mFab.getVisibility() == View.VISIBLE){
                    mFab.setAlpha(1.0f - slideOffset);
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavigationView);

        if(drawerOpen){
            mFab.setClickable(false);
            mFab.setVisibility(View.INVISIBLE);
        }
        else{
            mFab.setAlpha(1.0f);
            mFab.setVisibility(View.VISIBLE);
            mFab.setClickable(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onProjectClicked(Project project) {
        Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
        intent.putExtra(ProjectDetailsFragment.EXTRA_PROJECT, project);
        startActivity(intent);
    }

    @Override
    public void onTaskClicked(final Task task) {
        if(task.getId() == -1){
            DialogFragment dialog = new CreateTaskDialog();
            ((CreateTaskDialog) dialog).setListener(new CreateTaskDialog.OnCreateTaskListener() {
                @Override
                public void onCreateTask(Task saveTask) {
                    saveTask.setProject(task.getProject());
                    new SaveTaskAsync().execute(saveTask);
                }
            });
            dialog.show(getSupportFragmentManager(), "task_dialog");
        }
        else {
            startTask(task);
        }
    }

    @Override
    public boolean isPlayedTask(Task task) {
        return (mPlayedTimer != null) && (mPlayedTimer.getTask().equals(task));
    }

    private void restartProjects(){
        if(mProjectFragment != null){
            mProjectFragment.restartProjects();
        }
    }

    private void startTask(Task task){
        if(mPlayedTimer == null){
            //TODO save start time

            mPlayedTimer = new mobi.dende.simpletimesheet.model.Timer();
            mPlayedTimer.setTask(task);
            mPlayedTimer.setStartTime(new Date());
        }
        else if(mPlayedTimer.getTask().equals(task)){
            //TODO save end time
            mPlayedTimer = null;
        }
        else{
            //TODO save end time to old timer and save start time
            mPlayedTimer = new mobi.dende.simpletimesheet.model.Timer();
            mPlayedTimer.setTask(task);
            mPlayedTimer.setStartTime(new Date());
        }

        showTask();
    }

    private void showTask(){
        if(doAsynchronousTask != null){
            doAsynchronousTask.cancel();
            timer.purge();
        }

        if(mPlayedTimer != null) {
            mCollapsingBar.setBackgroundColor(mPlayedTimer.getTask().getColor());

            mDuration.setVisibility(View.VISIBLE);
            mTaskName.setVisibility(View.VISIBLE);

            mProjectName.setText(mPlayedTimer.getTask().getProject().getName());
            mTaskName.setText(mPlayedTimer.getTask().getName());

            doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if (mDuration != null) {
                                if (mPlayedTimer != null) {
                                    long actualTime = Calendar.getInstance().getTimeInMillis();
                                    long insertedTime = 0;
                                    if (mPlayedTimer.getStartTime() != null) {
                                        insertedTime = mPlayedTimer.getStartTime().getTime();
                                    }

                                    long size = (actualTime - insertedTime) / 1000;

                                    String value = "";

                                    if (size > 0 && size < 60) {
                                        value = String.format("%02ds", (int) size);
                                    } else if (size < 86400) {
                                        int hour = (int) (size / 3600);
                                        int minute = (int) (size % 3600) / 60;

                                        value = String.format("%02d:%02d", hour, minute);
                                    } else if (size >= 86400) {
                                        int day = (int) (size / 86400);

                                        if (day == 1) {
                                            value = String.format("%2d day", day);
                                        } else {
                                            value = String.format("%2d days", day);
                                        }
                                    }

                                    mDuration.setText(String.format(getString(R.string.task_duration), value));
                                } else {

                                }

                            }
                        }
                    });
                }
            };

            timer.schedule(doAsynchronousTask, 1000, 1000);
        }
        else{
            mCollapsingBar.setBackgroundResource(R.color.colorPrimary);

            mDuration.setVisibility(View.INVISIBLE);
            mTaskName.setVisibility(View.INVISIBLE);

            mProjectName.setText(R.string.info_start_time);
            mTaskName.setText("");
            mDuration.setText("");
        }
    }

    private class SaveProjectAsync extends AsyncTask<Project, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Project... params) {
            return TimesheetManager.addProject(MainActivity.this, params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result){
                restartProjects();
            }
        }
    }

    private class SaveTaskAsync extends AsyncTask<Task, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Task... params) {
            return TimesheetManager.addTask(MainActivity.this, params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result){
                restartProjects();
            }
        }
    }
}
