package com.rapid.furnitureaugmentreal.domain;



import java.io.Serializable;

public class UserAddress  implements Serializable {

    String id;
String Name;
String Flatno;
String Housename;
String LandMark;
String district;
String state;
String country;
String Pincode;
String Mobilenumber;
int selected=0;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFlatno() {
        return Flatno;
    }

    public void setFlatno(String flatno) {
        Flatno = flatno;
    }

    public String getHousename() {
        return Housename;
    }

    public void setHousename(String housename) {
        Housename = housename;
    }

    public String getLandMark() {
        return LandMark;
    }

    public void setLandMark(String landMark) {
        LandMark = landMark;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getMobilenumber() {
        return Mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        Mobilenumber = mobilenumber;
    }

    public UserAddress() {
    }
}
