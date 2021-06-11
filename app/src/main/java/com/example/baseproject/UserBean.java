package com.example.baseproject;

import android.os.Parcel;
import android.os.Parcelable;

public class UserBean implements Parcelable {
    public String name;
    public UserFav fav;
    public  static  class UserFav implements Parcelable {
        public String fruit;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.fruit);
        }

        public void readFromParcel(Parcel source) {
            this.fruit = source.readString();
        }

        public UserFav() {
        }

        protected UserFav(Parcel in) {
            this.fruit = in.readString();
        }

        public static final Creator<UserFav> CREATOR = new Creator<UserFav>() {
            @Override
            public UserFav createFromParcel(Parcel source) {
                return new UserFav(source);
            }

            @Override
            public UserFav[] newArray(int size) {
                return new UserFav[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.fav, flags);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.fav = source.readParcelable(UserFav.class.getClassLoader());
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.name = in.readString();
        this.fav = in.readParcelable(UserFav.class.getClassLoader());
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
