package mobi.dende.simpletimesheet.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.controller.TimesheetManager;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.Task;
import mobi.dende.simpletimesheet.ui.OnProjectScreenListener;
import mobi.dende.simpletimesheet.ui.adapter.ProjectAdapter;

/**
 * Manage part of screen about Projects and Tasks
 */
public class ProjectListFragment extends Fragment implements ExpandableListView.OnGroupClickListener,
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
        mAdapter.setListener(mMainListener);

        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(ProjectListFragment.this);
        mListView.setOnChildClickListener(ProjectListFragment.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mListView.setNestedScrollingEnabled(true);
        }

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mAdapter.getGroupCount() == 0){
            restartProjects();
        }
    }

    public void restartProjects(){
        ProjectsAsync async = new ProjectsAsync();
        async.execute();
    }

    private void downloadTasks(List<Project> projects){
        TasksAsync async = new TasksAsync();
        async.execute(projects);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        mMainListener.onProjectClicked(mAdapter.getGroup(groupPosition));
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mMainListener.onTaskClicked(mAdapter.getChild(groupPosition, childPosition));
        mAdapter.notifyDataSetChanged();
        return true;
    }

    public void notifyChange(){
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ProjectsAsync extends AsyncTask<Void, Void, List<Project>> {

        @Override
        protected List<Project> doInBackground(Void... params) {
            return TimesheetManager.getProjects(getContext());
        }

        @Override
        protected void onPostExecute(List<Project> projects) {
            super.onPostExecute(projects);

            if((projects != null) && !projects.isEmpty()){
                mAdapter.setProjects(projects);
                downloadTasks(projects);
            }
        }
    }

    private class TasksAsync extends AsyncTask<List<Project>, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(List<Project>... params) {
            return TimesheetManager.getTasks(getContext(), params[0]);
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            super.onPostExecute(tasks);

            mAdapter.setTasks(tasks);
        }
    }
}
