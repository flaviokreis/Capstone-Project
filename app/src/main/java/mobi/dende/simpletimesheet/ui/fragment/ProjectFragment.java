package mobi.dende.simpletimesheet.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.Task;
import mobi.dende.simpletimesheet.ui.OnProjectScreenListener;
import mobi.dende.simpletimesheet.ui.adapter.ProjectAdapter;

/**
 * Manage part of screen about Projects and Tasks
 */
public class ProjectFragment extends Fragment implements ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener{

    private ExpandableListView mListView;

    private ProjectAdapter mAdapter;

    private OnProjectScreenListener mMainListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mMainListener = (OnProjectScreenListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnProjectScreenListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        mListView = (ExpandableListView) view.findViewById(R.id.expandable_project_list);

        mAdapter = new ProjectAdapter(getActivity());

        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(ProjectFragment.this);
        mListView.setOnChildClickListener(ProjectFragment.this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        temp();
    }

    private void temp(){
        List<Project> projectList = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        Project project = new Project();
        project.setId(1);
        project.setName("Project ABC");
        project.setColor(getResources().getColor(R.color.project_color_3));

        projectList.add(project);

        Task task = new Task();
        task.setId(1);
        task.setName("Task ABC 1");
        task.setProject(project);

        tasks.add(task);

        task = new Task();
        task.setId(2);
        task.setName("Task ABC 2");
        task.setProject(project);

        task = new Task();
        task.setId(3);
        task.setName("Task ABC 3");
        task.setProject(project);

        task = new Task();
        task.setId(4);
        task.setName("Task ABC 4");
        task.setProject(project);

        tasks.add(task);

        project = new Project();
        project.setId(2);
        project.setName("Project ASFDA");
        project.setColor(getResources().getColor(R.color.project_color_4));

        projectList.add(project);

        task = new Task();
        task.setId(5);
        task.setName("Task ASDFA 1");
        task.setProject(project);

        tasks.add(task);

        task = new Task();
        task.setId(6);
        task.setName("Task ASDFA 2");
        task.setProject(project);

        tasks.add(task);


        project = new Project();
        project.setId(12);
        project.setName("asdfasdf Project");
        project.setColor(getResources().getColor(R.color.project_color_5));

        projectList.add(project);

        task = new Task();
        task.setId(7);
        task.setName("Task ABC 1");
        task.setProject(project);

        tasks.add(task);

        task = new Task();
        task.setId(22);
        task.setName("Task ABC 2");
        task.setProject(project);

        tasks.add(task);

        mAdapter.setProjects(projectList);
        mAdapter.setTasks(tasks);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mListView.setNestedScrollingEnabled(true);
        }

    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        mMainListener.onProjectClicked(mAdapter.getGroup(groupPosition));
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mMainListener.onTaskClicked(mAdapter.getGroupId(groupPosition), id);
        return true;
    }
}
