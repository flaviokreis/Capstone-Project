package mobi.dende.simpletimesheet.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobi.dende.simpletimesheet.R;
import mobi.dende.simpletimesheet.model.Project;
import mobi.dende.simpletimesheet.model.Task;
import mobi.dende.simpletimesheet.ui.OnProjectScreenListener;

/**
 * Adapter to show expandable list of projects and tasks
 */
public class ProjectAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private OnProjectScreenListener mListener;

    private List<Project> mProjects;
    private Map<Project, List<Task>> mTasks;

    public ProjectAdapter(Context context){
        mContext = context;

        mProjects = new ArrayList<>();
        mTasks = new HashMap<>();
    }

    public void setListener(OnProjectScreenListener listener){
        mListener = listener;
    }

    public void setProjects(List<Project> projects){
        if((projects != null) && !projects.isEmpty()){
            mProjects.clear();
            mTasks.clear();
            mProjects.addAll(projects);
            for(Project project : mProjects){
                mTasks.put(project, new ArrayList<Task>());
            }
        }
        notifyDataSetChanged();
    }

    public void setTasks(List<Task> tasks){
        if((tasks != null) && !tasks.isEmpty()){
            for(Task task : tasks){
                List<Task> list = mTasks.get(task.getProject());
                if(list != null){
                    list.add(task);
                }
            }
        }

        //insert Add Task item of list
        for(Project project : mProjects){
            List<Task> list = mTasks.get(project);
            if(list != null){
                list.add(new Task(mContext, project));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mProjects.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTasks.get(mProjects.get(groupPosition)).size();
    }

    @Override
    public Project getGroup(int groupPosition) {
        return mProjects.get(groupPosition);
    }

    @Override
    public Task getChild(int groupPosition, int childPosition) {
        return mTasks.get(mProjects.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroup(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).getId();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new GroupViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_project, null);

            viewHolder.title = (TextView)convertView.findViewById(R.id.project_title);

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (GroupViewHolder)convertView.getTag();
        }

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        viewHolder.title.setText(getGroup(groupPosition).getName());
        viewHolder.title.setContentDescription(String.format(mContext.getString(R.string.project_description), getGroup(groupPosition).getName()));

        return convertView;
    }

    private static class GroupViewHolder{
        public TextView title;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ChildViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_task, null);

            viewHolder.title = (TextView)convertView.findViewById(R.id.task_title);
            viewHolder.pause = (ImageView)convertView.findViewById(R.id.task_pause_icon);

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ChildViewHolder)convertView.getTag();
        }

        Task task = getChild(groupPosition, childPosition);

        viewHolder.title.setText(task.getName());
        viewHolder.title.setContentDescription(String.format(mContext.getString(R.string.task_description), task.getName()));

        if((mListener != null) && (mListener.isPlayedTaskId() != 0) && (mListener.isPlayedTaskId() == task.getId())){
            viewHolder.title.setTextColor(task.getColor());
            viewHolder.pause.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.primaryText));
            viewHolder.pause.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ChildViewHolder{
        public TextView title;
        public ImageView pause;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
