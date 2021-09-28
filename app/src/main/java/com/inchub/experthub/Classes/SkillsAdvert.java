package com.inchub.experthub.Classes;

public class SkillsAdvert {
    private String location;
    private String contactNumber;
    private String workDesc;


    public SkillsAdvert(String location, String contactNumber, String workDesc) {

        this.location = location;
        this.contactNumber = contactNumber;
        this.workDesc = workDesc;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }
}
