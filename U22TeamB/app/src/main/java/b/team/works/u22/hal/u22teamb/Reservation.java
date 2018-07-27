package b.team.works.u22.hal.u22teamb;

import java.io.Serializable;

public class Reservation implements Serializable {

    private String id;
    private String name;
    private String menuNo;
    private String date;
    private String time;

    private String errorMenuNo;
    private String errorDate;
    private String errorTime;

    private boolean hasNoError;

    public Reservation() {
        this.id = "";
        this.name = "";
        this.menuNo = "";
        this.date = "";
        this.time = "";

        this.errorMenuNo = "";
        this.errorDate = "";
        this.errorTime = "";

        hasNoError = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getErrorMenuNo() {
        return errorMenuNo;
    }

    public void setErrorMenuNo(String errorMenuNo) {
        this.errorMenuNo = errorMenuNo;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }

    public boolean isHasNoError() {
        return hasNoError;
    }

    public void setHasNoError(boolean hasError) {
        this.hasNoError = hasError;
    }
}
