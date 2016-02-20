package mobi.dende.simpletimesheet.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
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

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.Task;
import mobi.dende.simpletimesheet.ui.fragment.ProjectFragment;

public class MainActivity extends AppCompatActivity implements OnProjectScreenListener {

    private ProjectFragment mProjectFragment;

    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            mProjectFragment = new ProjectFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, mProjectFragment)
                    .commit();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new CreateProjectDialog();
                ((CreateProjectDialog)dialog).setListener(new CreateProjectDialog.OnCreateProjectListener() {
                    @Override
                    public void onCreateProject(Project project) {
                        //TODO need save project
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

                        switch (menuItem.getItemId()){
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
    public void onProjectClicked(long id) {
        Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskClicked(long projectId, long taskId) {
        if(taskId == -1){
            DialogFragment dialog = new CreateTaskDialog();
            ((CreateTaskDialog) dialog).setListener(new CreateTaskDialog.OnCreateTaskListener() {
                @Override
                public void onCreateTask(Task task) {
                    //TODO save new task
                }
            });
            dialog.show(getSupportFragmentManager(), "task_dialog");
        }
        else {
            //TODO
            //1. start if not have other task started;
            //2. if the same task, stop;
            //3. if other task, show a dialog to confirm a change;
        }
    }
}
