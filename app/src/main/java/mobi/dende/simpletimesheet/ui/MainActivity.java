package mobi.dende.simpletimesheet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.Task;
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
