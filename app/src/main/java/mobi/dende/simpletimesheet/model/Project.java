package mobi.dende.simpletimesheet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Project item
 */
public class Project implements Parcelable {
    private long id;
    private String name;
    private float valueHour;
    private String description;
    private int color;
    private Date insertedDate;
    private Date updatedDate;

    public Project(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValueHour() {
        return valueHour;
    }

    public void setValueHour(float valueHour) {
        this.valueHour = valueHour;
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
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        if (id != project.id) return false;
        return name.equals(project.name);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }

    protected Project(Parcel in) {
        id = in.readLong();
        name = in.readString();
        valueHour = in.readFloat();
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
        dest.writeString(name);
        dest.writeFloat(valueHour);
        dest.writeString(description);
        dest.writeInt(color);
        dest.writeLong(insertedDate != null ? insertedDate.getTime() : -1L);
        dest.writeLong(updatedDate != null ? updatedDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
