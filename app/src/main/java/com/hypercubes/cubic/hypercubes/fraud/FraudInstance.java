package com.hypercubes.cubic.hypercubes.fraud;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * An instance of detected fraud
 */
public class FraudInstance implements Parcelable {
    public String timestamp;
    public String pic_url;
    public float latitude;
    public float longitude;
    public String bus_line;
    public String location;
    public Integer vehicle_id;// Bus that this happened on
    public Integer operator_id;
    public Integer next_stop;

    public FraudInstance(){

    }

    public void setDefault(){
        this.timestamp = "10:09:44";
        this.pic_url = "http://www.dummyurl.com";
        this.latitude = (float) 32.715;
        this.longitude = (float) -117.161;
        this.bus_line = "Green";
        this.location= "San Diego";
        this.vehicle_id = 1;
        this.operator_id = 1;
        this.next_stop = 1;
    }

    protected FraudInstance(Parcel in) {
        timestamp = in.readString();
        pic_url = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        bus_line = in.readString();
        location = in.readString();
        if (in.readByte() == 0) {
            vehicle_id = null;
        } else {
            vehicle_id = in.readInt();
        }
        if (in.readByte() == 0) {
            operator_id = null;
        } else {
            operator_id = in.readInt();
        }
        if (in.readByte() == 0) {
            next_stop = null;
        } else {
            next_stop = in.readInt();
        }
    }

    public static final Creator<FraudInstance> CREATOR = new Creator<FraudInstance>() {
        @Override
        public FraudInstance createFromParcel(Parcel in) {
            return new FraudInstance(in);
        }

        @Override
        public FraudInstance[] newArray(int size) {
            return new FraudInstance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timestamp);
        dest.writeString(pic_url);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeString(bus_line);
        dest.writeString(location);
        dest.writeInt(vehicle_id);
        dest.writeInt(operator_id);
        dest.writeInt(next_stop);
    }
}
