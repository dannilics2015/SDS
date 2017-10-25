package model;

public class MyRecord {
    private String resortID;
    private String dayNum;
    private String skierID;
    private String liftID;
    private String timestamp;

    public MyRecord() {}


    public String getResortID() {
        return resortID;
    }

    public String getDayNum() {
        return dayNum;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSkierID() {
        return skierID;
    }

    public String getLiftID() {
        return liftID;
    }

    public void setResortID(String resortID) {
        this.resortID = resortID;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSkierID(String skierID) {
        this.skierID = skierID;
    }

    public void setLiftID(String liftID) {
        this.liftID = liftID;
    }

    @Override
    public String toString() {
        return resortID + ',' + dayNum + ',' + liftID + ',' + timestamp;
    }
}
