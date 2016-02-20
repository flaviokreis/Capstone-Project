package mobi.dende.simpletimesheet.ui;

import mobi.dende.simpletimesheet.model.Project;

/**
 * Created by flaviokreis on 14/02/16.
 */
public interface OnProjectScreenListener {
    void onProjectClicked(Project project);
    void onTaskClicked(long projectId, long taskId);
}
