package mobi.dende.simpletimesheet.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.ui.fragment.ProjectDetailsFragment;

public class ProjectActivity extends AppCompatActivity {

    private ProjectDetailsFragment mProjectFragment;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mToolbarLayout;
    private AppBarLayout mAppBar;

    private Project mProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        mProject = getIntent().getExtras().getParcelable(ProjectDetailsFragment.EXTRA_PROJECT);

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
            mToolbarLayout.setBackgroundColor(mProject.getColor());
            mToolbar.setTitle(mProject.getName());
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
