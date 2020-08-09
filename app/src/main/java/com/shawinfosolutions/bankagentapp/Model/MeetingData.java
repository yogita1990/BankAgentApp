package com.shawinfosolutions.bankagentapp.Model;

public class MeetingData {
    private String meetingId,mobileNo,dateTime;

    public MeetingData() {
    }

    public MeetingData(String meetingId, String mobileNo, String dateTime) {
        this.meetingId = meetingId;
        this.mobileNo = mobileNo;
        this.dateTime = dateTime;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
