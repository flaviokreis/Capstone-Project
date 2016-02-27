package mobi.dende.simpletimesheet.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.ui.fragment.ProjectDetailsFragment;
import mobi.dende.simpletimesheet.util.Utils;

public class ProjectActivity extends AppCompatActivity {

    private ProjectDetailsFragment mProjectFragment;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mToolbarLayout;
    private AppBarLayout mAppBar;

    private Project mProject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mProject = savedInstanceState.getParcelable(ProjectDetailsFragment.EXTRA_PROJECT);
        }
        else{
            mProject = getIntent().getExtras().getParcelable(ProjectDetailsFragment.EXTRA_PROJECT);
        }

        setTheme(Utils.getProjectTheme(ProjectActivity.this, mProject));

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_project);

        if(savedInstanceState == null){
            mProjectFragment = new ProjectDetailsFragment();
            mProjectFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.project_main_content, mProjectFragment)
                    .commit();
        }


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);

        if(mProject != null){
            mToolbar.setTitle(mProject.getName());
        }

        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ProjectDetailsFragment.EXTRA_PROJECT, mProject);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
