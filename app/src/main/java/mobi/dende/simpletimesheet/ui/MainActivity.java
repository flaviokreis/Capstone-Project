package mobi.dende.simpletimesheet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.ui.fragment.ProjectFragment;

public class MainActivity extends AppCompatActivity implements OnProjectScreenListener {

    private View mainContent;

    private ProjectFragment mProjectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContent = findViewById(R.id.main_content);

        if(savedInstanceState == null){
            mProjectFragment = new ProjectFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, mProjectFragment)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO create a add project dialog
            }
        });
    }

    @Override
    public void onProjectClicked(long id) {
        Log.d("MainActivity", "Project id: " + id);
        Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskClicked(long projectId, long taskId) {
        Log.d("MainActivity", "Project id: " + projectId + ", task id: " + taskId);
        if(taskId == -1){
            //TODO open create new task dialog
        }
        else {
            //TODO
            //1. start if not have other task started;
            //2. if the same task, stop;
            //3. if other task, show a dialog to confirm a change;
        }
    }
}
