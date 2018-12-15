package com.example.chen.intelligentweigh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author chen
 * @date 2018/12/13.   21:26
 * description：
 */
public class House2 implements Parcelable {

    private String ID;
    private String name;
    private String areas;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "House2{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", areas='" + areas + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(name);
        dest.writeString(areas);

    }


    public static final Parcelable.Creator<House2> CREATOR = new Parcelable.Creator<House2>() {
        public House2 createFromParcel(Parcel in) {
            return new House2(in);
        }

        public House2[] newArray(int size) {
            return new House2[size];
        }
    };

    private House2(Parcel in) {
        this.ID = in.readString();
        this.name = in.readString();
        this.areas = in.readString();
    }

    public House2() {
        super();
    }

    public House2(String ID, String name, String areas) {
        super();
        this.ID = ID;
        this.name = name;
        this.areas = areas;
      
    }

}
