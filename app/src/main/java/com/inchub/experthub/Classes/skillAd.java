package com.inchub.experthub.Classes;

public class skillAd {

    private String PhoneNo;
    private String WorkDesc;
    private String Location;
    private String pic;
    private String userId;
    private String name;


    public skillAd() {
    }

    public skillAd(String phoneNo, String workDesc, String location, String pic, String userId, String name) {
        PhoneNo = phoneNo;
        WorkDesc = workDesc;
        Location = location;
        this.name = name;
        this.pic = pic;
        this.userId = userId;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }


    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getWorkDesc() {
        return WorkDesc;
    }

    public void setWorkDesc(String workDesc) {
        WorkDesc = workDesc;
    }


    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
