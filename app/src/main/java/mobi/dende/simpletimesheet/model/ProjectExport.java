package mobi.dende.simpletimesheet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by flaviokreis on 28/02/16.
 */
public class ProjectExport implements Parcelable {

    private Project project;

    private Date startDate;
    private Date endDate;

    private List<Timer> timers;

    private int totalHours;

    public ProjectExport(){

    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Timer> getTimers() {
        return timers;
    }

    public void setTimers(List<Timer> timers) {
        this.timers = timers;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    protected ProjectExport(Parcel in) {
        project = (Project) in.readValue(Project.class.getClassLoader());
        long tmpStartDate = in.readLong();
        startDate = tmpStartDate != -1 ? new Date(tmpStartDate) : null;
        long tmpEndDate = in.readLong();
        endDate = tmpEndDate != -1 ? new Date(tmpEndDate) : null;
        if (in.readByte() == 0x01) {
            timers = new ArrayList<>();
            in.readList(timers, Timer.class.getClassLoader());
        } else {
            timers = null;
        }
        totalHours = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(project);
        dest.writeLong(startDate != null ? startDate.getTime() : -1L);
        dest.writeLong(endDate != null ? endDate.getTime() : -1L);
        if (timers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(timers);
        }
        dest.writeInt(totalHours);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProjectExport> CREATOR = new Parcelable.Creator<ProjectExport>() {
        @Override
        public ProjectExport createFromParcel(Parcel in) {
            return new ProjectExport(in);
        }

        @Override
        public ProjectExport[] newArray(int size) {
            return new ProjectExport[size];
        }
    };
}