package com.gridviewimagepicker.model;

import java.io.Serializable;

public class Users implements Serializable {
    String mName, userid, Uri, name, sex, location, phoneNo, phoneNo2, profession, about, address;
    double longitude, latitude;

    public Users() {}

    public Users(String mName, String userid, String Uri, String name, String sex, String location,
                 String phoneNo, String phoneNo2, String profession, String about, String address) {
        this.mName = mName;
        this.userid = userid;
        this.Uri = Uri;
        this.name = name;
        this.sex = sex;
        this.location = location;
        this.phoneNo = phoneNo;
        this.phoneNo2 = phoneNo2;
        this.profession = profession;
        this.about = about;
        this.address = address;
    }

    public Users(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        this.Uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo2() {
        return phoneNo2;
    }

    public void setPhoneNo2(String phoneNo2) {
        this.phoneNo2 = phoneNo2;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
