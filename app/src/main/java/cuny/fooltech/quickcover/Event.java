package cuny.fooltech.quickcover;

import java.io.Serializable;

/**
 * Created by kevin on 4/29/2017.
 */

public class Event implements Serializable {
    private String name;
    private int day;
    private int startTime;
    private int endTime;
    private int month;
    private boolean needCover;
    private String position;
    private int year;

    public Event(String name, String position, int day, int month, int year, int startTime, int endTime, boolean needCover) {
        this.name = name;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.month = month;
        this.needCover = needCover;
        this.position = position;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isNeedCover() {
        return needCover;
    }

    public void setNeedCover(boolean needCover) {
        this.needCover = needCover;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }


    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
