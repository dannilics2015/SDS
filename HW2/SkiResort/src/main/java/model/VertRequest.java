package model;

public class VertRequest {
    private String skierID;
    private String dayNum;

    public VertRequest() {
    }

    public void setSkierID(String skierID) {
        this.skierID = skierID;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public String getSkierID() {
        return skierID;
    }

    public String getDayNum() {
        return dayNum;
    }
}
