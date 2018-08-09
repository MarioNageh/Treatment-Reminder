package com.marionageh.treatmentreminder.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "Treatment")
public class Treatment implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String note;
    private String date;
    private String time;
    private boolean repeat;
    private String repeat_no;
    private String repeat_type;
    private boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getRepeat_no() {
        return repeat_no;
    }

    public void setRepeat_no(String repeat_no) {
        this.repeat_no = repeat_no;
    }

    public String getRepeat_type() {
        return repeat_type;
    }

    public void setRepeat_type(String repeat_type) {
        this.repeat_type = repeat_type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Treatment(String name, String note, String date, String time, boolean repeat, String repeat_no, String repeat_type, boolean active) {

        this.name = name;
        this.note = note;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
        this.repeat_no = repeat_no;
        this.repeat_type = repeat_type;
        this.active = active;
    }

    protected Treatment(Parcel in) {
        id = in.readInt();
        name = in.readString();
        note = in.readString();
        date = in.readString();
        time = in.readString();
        repeat = in.readByte() != 0;
        repeat_no = in.readString();
        repeat_type = in.readString();
        active = in.readByte() != 0;
    }

    public static final Creator<Treatment> CREATOR = new Creator<Treatment>() {
        @Override
        public Treatment createFromParcel(Parcel in) {
            return new Treatment(in);
        }

        @Override
        public Treatment[] newArray(int size) {
            return new Treatment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(note);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeByte((byte) (repeat ? 1 : 0));
        dest.writeString(repeat_no);
        dest.writeString(repeat_type);
        dest.writeByte((byte) (active ? 1 : 0));
    }
}