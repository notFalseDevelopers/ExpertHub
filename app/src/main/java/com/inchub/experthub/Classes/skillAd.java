package com.inchub.experthub.Classes;

public class skillAd {

    private String phoneNo, workDesc, location, jobCount, Uri, advertId;

    public skillAd(String phoneNo, String workDesc, String location, String jobCount, String advertId, String uri) {
        this.phoneNo = phoneNo;
        this.workDesc = workDesc;
        this.location = location;
        this.jobCount = jobCount;
        this.Uri = uri;
        this.advertId = advertId;
    }

    public void setAdvertId(String advertId){this.advertId = advertId;}

    public String getAdvertId(){return advertId;}

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobCount() {
        return jobCount;
    }

    public void setJobCount(String jobCount) {
        this.jobCount = jobCount;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }





}
