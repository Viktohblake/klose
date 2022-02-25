package com.gridviewimagepicker;

import android.net.Uri;

public class Users {
    String mName, userid, profile, url, Uri, name, sex, location, phoneNo, phoneNo2, profession, aboutMe;

    public Users() {}

    public Users(String mName, String userid, String profile, String url,
                 String Uri, String name, String sex, String location,
                 String phoneNo, String phoneNo2, String profession, String aboutMe) {
        this.mName = mName;
        this.userid = userid;
        this.profile = profile;
        this.url = url;
        this.Uri = Uri;
        this.name = name;
        this.sex = sex;
        this.location = location;
        this.phoneNo = phoneNo;
        this.phoneNo2 = phoneNo2;
        this.profession = profession;
        this.aboutMe = aboutMe;
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        this.Uri = Uri;
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

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
