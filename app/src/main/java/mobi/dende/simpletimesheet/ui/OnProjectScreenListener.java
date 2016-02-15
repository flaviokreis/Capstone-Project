package mobi.dende.simpletimesheet.ui;

/**
 * Created by flaviokreis on 14/02/16.
 */
public interface OnProjectScreenListener {
    void onProjectClicked(long id);
    void onTaskClicked(long projectId, long taskId);
}
