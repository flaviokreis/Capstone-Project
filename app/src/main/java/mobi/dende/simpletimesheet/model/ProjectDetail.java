package mobi.dende.simpletimesheet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetail implements Parcelable {
    private Project project;
    private List<Timer> timers;

    private List<Integer> months;
    private int maxHours;

    private float minutesToday;
    private float minutesWeek;
    private float minutesMonth;

    private float earnPerHour;
    private float earnMonth;
    private float earnTotal;

    public ProjectDetail(Project project){
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Timer> getTimers() {
        return timers;
    }

    public void setTimers(List<Timer> timers) {
        this.timers = timers;
    }

    public List<Integer> getMonths() {
        return months;
    }

    public void setMonths(List<Integer> months) {
        this.months = months;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public void setMaxHours(int maxHours) {
        this.maxHours = maxHours;
    }

    public float getMinutesToday() {
        return minutesToday;
    }

    public void setMinutesToday(float minutesToday) {
        this.minutesToday = minutesToday;
    }

    public float getMinutesWeek() {
        return minutesWeek;
    }

    public void setMinutesWeek(float minutesWeek) {
        this.minutesWeek = minutesWeek;
    }

    public float getMinutesMonth() {
        return minutesMonth;
    }

    public void setMinutesMonth(float minutesMonth) {
        this.minutesMonth = minutesMonth;
    }

    public float getEarnPerHour() {
        return earnPerHour;
    }

    public void setEarnPerHour(float earnPerHour) {
        this.earnPerHour = earnPerHour;
    }

    public float getEarnMonth() {
        return earnMonth;
    }

    public void setEarnMonth(float earnMonth) {
        this.earnMonth = earnMonth;
    }

    public float getEarnTotal() {
        return earnTotal;
    }

    public void setEarnTotal(float earnTotal) {
        this.earnTotal = earnTotal;
    }

    protected ProjectDetail(Parcel in) {
        project = (Project) in.readValue(Project.class.getClassLoader());
        if (in.readByte() == 0x01) {
            timers = new ArrayList<Timer>();
            in.readList(timers, Timer.class.getClassLoader());
        } else {
            timers = null;
        }
        if (in.readByte() == 0x01) {
            months = new ArrayList<Integer>();
            in.readList(months, Integer.class.getClassLoader());
        } else {
            months = null;
        }
        maxHours = in.readInt();
        minutesToday = in.readFloat();
        minutesWeek = in.readFloat();
        minutesMonth = in.readFloat();
        earnPerHour = in.readFloat();
        earnMonth = in.readFloat();
        earnTotal = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(project);
        if (timers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(timers);
        }
        if (months == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(months);
        }
        dest.writeInt(maxHours);
        dest.writeFloat(minutesToday);
        dest.writeFloat(minutesWeek);
        dest.writeFloat(minutesMonth);
        dest.writeFloat(earnPerHour);
        dest.writeFloat(earnMonth);
        dest.writeFloat(earnTotal);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProjectDetail> CREATOR = new Parcelable.Creator<ProjectDetail>() {
        @Override
        public ProjectDetail createFromParcel(Parcel in) {
            return new ProjectDetail(in);
        }

        @Override
        public ProjectDetail[] newArray(int size) {
            return new ProjectDetail[size];
        }
    };
}