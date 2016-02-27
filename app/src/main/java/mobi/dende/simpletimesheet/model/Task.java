package mobi.dende.simpletimesheet.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import mobi.dende.simpletimesheet.R;

/**
 * Task item
 */
public class Task implements Parcelable {
    private long id;
    private Project project;
    private String name;
    private String description;
    private int color;
    private Date insertedDate;
    private Date updatedDate;

    public Task(){

    }

    //To create special button
    public Task(Context context, Project project){
        this.project = project;
        this.color = project.getColor();
        this.name = context.getString(R.string.add_new_task);
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getInsertedDate() {
        return insertedDate;
    }

    public void setInsertedDate(Date insertedDate) {
        this.insertedDate = insertedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!project.equals(task.project)) return false;
        return name.equals(task.name);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + project.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    protected Task(Parcel in) {
        id = in.readLong();
        project = (Project) in.readValue(Project.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        color = in.readInt();
        long tmpInsertedDate = in.readLong();
        insertedDate = tmpInsertedDate != -1 ? new Date(tmpInsertedDate) : null;
        long tmpUpdatedDate = in.readLong();
        updatedDate = tmpUpdatedDate != -1 ? new Date(tmpUpdatedDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeValue(project);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(color);
        dest.writeLong(insertedDate != null ? insertedDate.getTime() : -1L);
        dest.writeLong(updatedDate != null ? updatedDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", project=" + project +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color=" + color +
                ", insertedDate=" + insertedDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
